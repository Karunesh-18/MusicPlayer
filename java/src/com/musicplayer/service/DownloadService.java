package com.musicplayer.service;

import com.musicplayer.model.*;
import com.musicplayer.util.PythonBridge;
import java.util.*;
import java.util.concurrent.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service class for managing music downloads with queue management,
 * progress tracking, and integration with Python backend.
 */
public class DownloadService {
    private final PythonBridge pythonBridge;
    private final ExecutorService downloadExecutor;
    private final Map<String, DownloadTask> activeDownloads;
    private final Queue<DownloadTask> downloadQueue;
    private final List<DownloadListener> listeners;
    private final String downloadDirectory;
    private boolean isProcessingQueue;

    // Download task representation
    public static class DownloadTask {
        private String id;
        private String query;
        private User requestedBy;
        private DownloadStatus status;
        private double progress;
        private String errorMessage;
        private Song resultSong;
        private long startTime;
        private long endTime;

        public enum DownloadStatus {
            QUEUED, DOWNLOADING, COMPLETED, FAILED, CANCELLED
        }

        public DownloadTask(String query, User requestedBy) {
            this.id = "download_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
            this.query = query;
            this.requestedBy = requestedBy;
            this.status = DownloadStatus.QUEUED;
            this.progress = 0.0;
            this.startTime = System.currentTimeMillis();
        }

        // Getters and setters
        public String getId() { return id; }
        public String getQuery() { return query; }
        public User getRequestedBy() { return requestedBy; }
        public DownloadStatus getStatus() { return status; }
        public void setStatus(DownloadStatus status) { this.status = status; }
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = Math.max(0.0, Math.min(100.0, progress)); }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public Song getResultSong() { return resultSong; }
        public void setResultSong(Song resultSong) { this.resultSong = resultSong; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        
        public long getDurationMs() {
            return endTime > startTime ? endTime - startTime : System.currentTimeMillis() - startTime;
        }
    }

    // Download progress listener interface
    public interface DownloadListener {
        void onDownloadStarted(DownloadTask task);
        void onDownloadProgress(DownloadTask task, double progress);
        void onDownloadCompleted(DownloadTask task, Song song);
        void onDownloadFailed(DownloadTask task, String error);
        void onDownloadCancelled(DownloadTask task);
    }

    public DownloadService(PythonBridge pythonBridge) {
        this.pythonBridge = pythonBridge;
        this.downloadExecutor = Executors.newFixedThreadPool(3); // Max 3 concurrent downloads
        this.activeDownloads = new ConcurrentHashMap<>();
        this.downloadQueue = new ConcurrentLinkedQueue<>();
        this.listeners = new ArrayList<>();
        this.downloadDirectory = "./downloads";
        this.isProcessingQueue = false;
        
        // Ensure download directory exists
        createDownloadDirectory();
        
        // Start queue processor
        startQueueProcessor();
    }

    // Public API Methods
    public DownloadTask queueDownload(String query, User user) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Download query cannot be null or empty");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check if song already exists
        Song existingSong = findExistingSong(query);
        if (existingSong != null && existingSong.isDownloaded()) {
            // Create a completed task for existing song
            DownloadTask task = new DownloadTask(query, user);
            task.setStatus(DownloadTask.DownloadStatus.COMPLETED);
            task.setProgress(100.0);
            task.setResultSong(existingSong);
            task.setEndTime(System.currentTimeMillis());
            
            // Notify listeners
            notifyDownloadCompleted(task, existingSong);
            return task;
        }

        DownloadTask task = new DownloadTask(query, user);
        downloadQueue.offer(task);
        
        System.out.println("🎵 Queued download: " + query + " (Queue size: " + downloadQueue.size() + ")");
        
        processQueue();
        return task;
    }

    public boolean cancelDownload(String taskId) {
        DownloadTask task = activeDownloads.get(taskId);
        if (task != null && task.getStatus() == DownloadTask.DownloadStatus.DOWNLOADING) {
            task.setStatus(DownloadTask.DownloadStatus.CANCELLED);
            task.setEndTime(System.currentTimeMillis());
            activeDownloads.remove(taskId);
            
            notifyDownloadCancelled(task);
            return true;
        }
        
        // Also check queue
        return downloadQueue.removeIf(t -> t.getId().equals(taskId));
    }

    public DownloadTask getDownloadStatus(String taskId) {
        DownloadTask task = activeDownloads.get(taskId);
        if (task != null) {
            return task;
        }
        
        // Check queue
        return downloadQueue.stream()
                           .filter(t -> t.getId().equals(taskId))
                           .findFirst()
                           .orElse(null);
    }

    public List<DownloadTask> getActiveDownloads() {
        return new ArrayList<>(activeDownloads.values());
    }

    public List<DownloadTask> getQueuedDownloads() {
        return new ArrayList<>(downloadQueue);
    }

    public int getQueueSize() {
        return downloadQueue.size();
    }

    public boolean isDownloading() {
        return !activeDownloads.isEmpty();
    }

    // Batch download operations
    public List<DownloadTask> queuePlaylistDownload(Playlist playlist, User user) {
        List<DownloadTask> tasks = new ArrayList<>();
        
        for (Song song : playlist.getSongs()) {
            if (!song.isDownloaded()) {
                String query = song.getArtist() + " " + song.getTitle();
                DownloadTask task = queueDownload(query, user);
                tasks.add(task);
            }
        }
        
        System.out.println("🎵 Queued playlist download: " + playlist.getName() + 
                          " (" + tasks.size() + " songs)");
        return tasks;
    }

    public List<DownloadTask> queueAlbumDownload(Album album, User user) {
        List<DownloadTask> tasks = new ArrayList<>();
        
        for (Song song : album.getTracks()) {
            if (!song.isDownloaded()) {
                String query = song.getArtist() + " " + song.getTitle();
                DownloadTask task = queueDownload(query, user);
                tasks.add(task);
            }
        }
        
        System.out.println("🎵 Queued album download: " + album.getDisplayName() + 
                          " (" + tasks.size() + " songs)");
        return tasks;
    }

    // Listener management
    public void addDownloadListener(DownloadListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeDownloadListener(DownloadListener listener) {
        listeners.remove(listener);
    }

    // Private implementation methods
    private void createDownloadDirectory() {
        try {
            Path downloadPath = Paths.get(downloadDirectory);
            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath);
                System.out.println("📁 Created download directory: " + downloadDirectory);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to create download directory: " + e.getMessage());
        }
    }

    private void startQueueProcessor() {
        Thread queueProcessor = new Thread(() -> {
            while (true) {
                try {
                    processQueue();
                    Thread.sleep(1000); // Check queue every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("❌ Queue processor error: " + e.getMessage());
                }
            }
        });
        queueProcessor.setDaemon(true);
        queueProcessor.setName("DownloadQueueProcessor");
        queueProcessor.start();
    }

    private synchronized void processQueue() {
        if (isProcessingQueue || activeDownloads.size() >= 3) {
            return; // Already processing or max concurrent downloads reached
        }
        
        isProcessingQueue = true;
        
        try {
            while (!downloadQueue.isEmpty() && activeDownloads.size() < 3) {
                DownloadTask task = downloadQueue.poll();
                if (task != null) {
                    startDownload(task);
                }
            }
        } finally {
            isProcessingQueue = false;
        }
    }

    private void startDownload(DownloadTask task) {
        activeDownloads.put(task.getId(), task);
        task.setStatus(DownloadTask.DownloadStatus.DOWNLOADING);
        
        notifyDownloadStarted(task);
        
        CompletableFuture.supplyAsync(() -> {
            try {
                return performDownload(task);
            } catch (Exception e) {
                task.setErrorMessage(e.getMessage());
                return null;
            }
        }, downloadExecutor).thenAccept(song -> {
            task.setEndTime(System.currentTimeMillis());
            activeDownloads.remove(task.getId());
            
            if (song != null) {
                task.setStatus(DownloadTask.DownloadStatus.COMPLETED);
                task.setProgress(100.0);
                task.setResultSong(song);
                notifyDownloadCompleted(task, song);
            } else {
                task.setStatus(DownloadTask.DownloadStatus.FAILED);
                if (task.getErrorMessage() == null) {
                    task.setErrorMessage("Download failed for unknown reason");
                }
                notifyDownloadFailed(task, task.getErrorMessage());
            }
            
            // Process next item in queue
            processQueue();
        });
    }

    private Song performDownload(DownloadTask task) {
        try {
            // Update progress
            task.setProgress(10.0);
            notifyDownloadProgress(task, 10.0);
            
            // Use Python bridge to download
            boolean success = pythonBridge.downloadSong(task.getQuery());
            
            if (!success) {
                throw new RuntimeException("Python download failed");
            }
            
            // Update progress
            task.setProgress(80.0);
            notifyDownloadProgress(task, 80.0);
            
            // Find the downloaded file
            String latestFile = pythonBridge.findLatestAudioFile();
            if (latestFile == null) {
                throw new RuntimeException("Downloaded file not found");
            }
            
            // Create Song object from downloaded file
            Song song = createSongFromFile(latestFile, task.getQuery());
            
            // Update progress
            task.setProgress(100.0);
            notifyDownloadProgress(task, 100.0);
            
            return song;
            
        } catch (Exception e) {
            System.err.println("❌ Download failed for '" + task.getQuery() + "': " + e.getMessage());
            throw e;
        }
    }

    private Song createSongFromFile(String filePath, String originalQuery) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File does not exist: " + filePath);
            }
            
            // Extract metadata from filename (basic implementation)
            String filename = file.getName();
            String[] parts = filename.replace(".mp3", "").split(" - ", 2);
            
            Song song = new Song();
            if (parts.length >= 2) {
                song.setArtist(parts[0].trim());
                song.setTitle(parts[1].trim());
            } else {
                // Fallback to original query
                String[] queryParts = originalQuery.split(" ", 2);
                if (queryParts.length >= 2) {
                    song.setArtist(queryParts[0].trim());
                    song.setTitle(Arrays.stream(queryParts).skip(1).reduce("", (a, b) -> a + " " + b).trim());
                } else {
                    song.setArtist("Unknown Artist");
                    song.setTitle(originalQuery);
                }
            }
            
            song.setFilePath(filePath);
            song.setDownloaded(true);
            song.setFileSize(file.length());
            song.setQuality("192k"); // Default quality
            
            return song;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to create song from file: " + e.getMessage());
            throw new RuntimeException("Failed to process downloaded file", e);
        }
    }

    private Song findExistingSong(String query) {
        // This would typically integrate with MusicLibraryService
        // For now, check if file exists in download directory
        try {
            File downloadDir = new File(downloadDirectory);
            if (!downloadDir.exists()) return null;
            
            File[] files = downloadDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".mp3") && 
                name.toLowerCase().contains(query.toLowerCase().substring(0, Math.min(query.length(), 10)))
            );
            
            if (files != null && files.length > 0) {
                return createSongFromFile(files[0].getAbsolutePath(), query);
            }
        } catch (Exception e) {
            // Ignore errors in finding existing songs
        }
        
        return null;
    }

    // Notification methods
    private void notifyDownloadStarted(DownloadTask task) {
        System.out.println("🔄 Started download: " + task.getQuery());
        listeners.forEach(listener -> {
            try {
                listener.onDownloadStarted(task);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyDownloadProgress(DownloadTask task, double progress) {
        listeners.forEach(listener -> {
            try {
                listener.onDownloadProgress(task, progress);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyDownloadCompleted(DownloadTask task, Song song) {
        System.out.println("✅ Completed download: " + task.getQuery() + 
                          " (" + (task.getDurationMs() / 1000.0) + "s)");
        listeners.forEach(listener -> {
            try {
                listener.onDownloadCompleted(task, song);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyDownloadFailed(DownloadTask task, String error) {
        System.out.println("❌ Failed download: " + task.getQuery() + " - " + error);
        listeners.forEach(listener -> {
            try {
                listener.onDownloadFailed(task, error);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyDownloadCancelled(DownloadTask task) {
        System.out.println("🚫 Cancelled download: " + task.getQuery());
        listeners.forEach(listener -> {
            try {
                listener.onDownloadCancelled(task);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    // Cleanup
    public void shutdown() {
        downloadExecutor.shutdown();
        try {
            if (!downloadExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                downloadExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            downloadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
