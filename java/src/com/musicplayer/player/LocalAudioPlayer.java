package com.musicplayer.player;

import com.musicplayer.model.Song;
import com.musicplayer.util.PythonBridge;
import java.io.File;
import java.util.concurrent.*;

/**
 * Concrete implementation of AudioPlayer for playing local audio files
 * using the Python backend integration.
 */
public class LocalAudioPlayer extends AudioPlayer {
    private final PythonBridge pythonBridge;
    private final ScheduledExecutorService positionTracker;
    private ScheduledFuture<?> positionUpdateTask;
    private volatile boolean isInitialized;

    public LocalAudioPlayer(PythonBridge pythonBridge) {
        super();
        this.pythonBridge = pythonBridge;
        this.positionTracker = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "AudioPlayer-PositionTracker");
            t.setDaemon(true);
            return t;
        });
        this.isInitialized = false;
        
        initialize();
    }

    private void initialize() {
        try {
            // Test Python bridge connection
            if (pythonBridge != null) {
                isInitialized = true;
                System.out.println("🎵 LocalAudioPlayer initialized successfully");
            } else {
                System.err.println("❌ PythonBridge is null - LocalAudioPlayer not initialized");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize LocalAudioPlayer: " + e.getMessage());
            isInitialized = false;
        }
    }

    @Override
    public boolean isSupported() {
        return isInitialized && pythonBridge != null;
    }

    @Override
    public boolean loadSong(Song song) {
        if (!isSupported() || song == null) {
            notifyError("Cannot load song: Player not supported or song is null");
            return false;
        }

        if (!song.isDownloaded() || song.getFilePath() == null) {
            notifyError("Cannot load song: Song not downloaded or file path is null");
            return false;
        }

        File audioFile = new File(song.getFilePath());
        if (!audioFile.exists() || !audioFile.canRead()) {
            notifyError("Cannot load song: Audio file does not exist or is not readable");
            return false;
        }

        try {
            // Stop current playback
            stop();
            
            Song oldSong = currentSong;
            PlaybackState oldState = state;
            
            // Load new song
            currentPositionSeconds = 0;
            notifySongChanged(oldSong, song);
            notifyStateChanged(oldState, PlaybackState.STOPPED);
            notifyPositionChanged(0);
            
            System.out.println("🎵 Loaded song: " + song.getDisplayName());
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to load song: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean play() {
        if (!isSupported()) {
            notifyError("Cannot play: Player not supported");
            return false;
        }

        if (currentSong == null) {
            notifyError("Cannot play: No song loaded");
            return false;
        }

        try {
            PlaybackState oldState = state;
            
            if (state == PlaybackState.PAUSED) {
                // Resume playback
                notifyStateChanged(oldState, PlaybackState.PLAYING);
                startPositionTracking();
                System.out.println("▶️ Resumed playback: " + currentSong.getDisplayName());
                return true;
            } else {
                // Start new playback
                notifyStateChanged(oldState, PlaybackState.BUFFERING);
                
                boolean success = pythonBridge.playAudioFile(currentSong.getFilePath());
                
                if (success) {
                    // Increment play count
                    currentSong.play();
                    
                    notifyStateChanged(PlaybackState.BUFFERING, PlaybackState.PLAYING);
                    startPositionTracking();
                    System.out.println("▶️ Started playback: " + currentSong.getDisplayName());
                    return true;
                } else {
                    notifyError("Failed to start playback");
                    notifyStateChanged(PlaybackState.BUFFERING, PlaybackState.ERROR);
                    return false;
                }
            }
            
        } catch (Exception e) {
            notifyError("Playback error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean pause() {
        if (!isSupported() || !isPlaying()) {
            return false;
        }

        try {
            PlaybackState oldState = state;
            stopPositionTracking();
            notifyStateChanged(oldState, PlaybackState.PAUSED);
            System.out.println("⏸️ Paused playback: " + 
                             (currentSong != null ? currentSong.getDisplayName() : "Unknown"));
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to pause: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean stop() {
        try {
            PlaybackState oldState = state;
            stopPositionTracking();
            currentPositionSeconds = 0;
            
            notifyStateChanged(oldState, PlaybackState.STOPPED);
            notifyPositionChanged(0);
            
            if (currentSong != null) {
                System.out.println("⏹️ Stopped playback: " + currentSong.getDisplayName());
            }
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to stop: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean seek(int positionSeconds) {
        if (!isSupported() || currentSong == null) {
            return false;
        }

        int duration = getDurationSeconds();
        if (positionSeconds < 0 || (duration > 0 && positionSeconds > duration)) {
            return false;
        }

        try {
            currentPositionSeconds = positionSeconds;
            notifyPositionChanged(positionSeconds);
            
            System.out.println("⏭️ Seeked to: " + getFormattedPosition() + 
                             " in " + currentSong.getDisplayName());
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to seek: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setVolume(double volume) {
        if (!isSupported()) {
            return false;
        }

        try {
            double oldVolume = this.volume;
            double newVolume = Math.max(0.0, Math.min(1.0, volume));
            
            // Note: Actual volume control would require more sophisticated
            // integration with the system audio or the Python backend
            notifyVolumeChanged(newVolume);
            
            System.out.println("🔊 Volume changed: " + (int)(oldVolume * 100) + "% → " + 
                             (int)(newVolume * 100) + "%");
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to set volume: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getDurationSeconds() {
        if (currentSong != null) {
            return currentSong.getDurationSeconds();
        }
        return 0;
    }

    // Position tracking
    private void startPositionTracking() {
        stopPositionTracking();
        
        positionUpdateTask = positionTracker.scheduleAtFixedRate(() -> {
            if (state == PlaybackState.PLAYING) {
                currentPositionSeconds++;
                notifyPositionChanged(currentPositionSeconds);
                
                // Check if song has ended
                int duration = getDurationSeconds();
                if (duration > 0 && currentPositionSeconds >= duration) {
                    // Song ended
                    stop();
                    // This would typically trigger the next song in PlaybackController
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void stopPositionTracking() {
        if (positionUpdateTask != null && !positionUpdateTask.isCancelled()) {
            positionUpdateTask.cancel(false);
            positionUpdateTask = null;
        }
    }

    // Enhanced playback methods
    public boolean fadeIn(int durationSeconds) {
        if (!isSupported() || currentSong == null) {
            return false;
        }

        try {
            // Start at 0 volume
            double originalVolume = volume;
            setVolume(0.0);
            
            if (!play()) {
                return false;
            }

            // Gradually increase volume
            ScheduledExecutorService fadeExecutor = Executors.newSingleThreadScheduledExecutor();
            double volumeStep = originalVolume / (durationSeconds * 10); // 10 steps per second
            
            fadeExecutor.scheduleAtFixedRate(() -> {
                double newVolume = volume + volumeStep;
                if (newVolume >= originalVolume) {
                    setVolume(originalVolume);
                    fadeExecutor.shutdown();
                } else {
                    setVolume(newVolume);
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to fade in: " + e.getMessage());
            return false;
        }
    }

    public boolean fadeOut(int durationSeconds) {
        if (!isSupported() || !isPlaying()) {
            return false;
        }

        try {
            double originalVolume = volume;
            double volumeStep = originalVolume / (durationSeconds * 10); // 10 steps per second
            
            ScheduledExecutorService fadeExecutor = Executors.newSingleThreadScheduledExecutor();
            
            fadeExecutor.scheduleAtFixedRate(() -> {
                double newVolume = volume - volumeStep;
                if (newVolume <= 0.0) {
                    setVolume(0.0);
                    stop();
                    setVolume(originalVolume); // Restore volume for next song
                    fadeExecutor.shutdown();
                } else {
                    setVolume(newVolume);
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            
            return true;
            
        } catch (Exception e) {
            notifyError("Failed to fade out: " + e.getMessage());
            return false;
        }
    }

    // Cleanup
    @Override
    public void cleanup() {
        super.cleanup();
        stopPositionTracking();
        
        if (positionTracker != null && !positionTracker.isShutdown()) {
            positionTracker.shutdown();
            try {
                if (!positionTracker.awaitTermination(2, TimeUnit.SECONDS)) {
                    positionTracker.shutdownNow();
                }
            } catch (InterruptedException e) {
                positionTracker.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("🎵 LocalAudioPlayer cleaned up");
    }

    // Diagnostic methods
    public boolean testPlayback() {
        if (!isSupported()) {
            System.out.println("❌ LocalAudioPlayer not supported");
            return false;
        }

        try {
            // Test with the latest audio file
            String latestFile = pythonBridge.findLatestAudioFile();
            if (latestFile != null) {
                Song testSong = new Song("Test", "Test Artist");
                testSong.setFilePath(latestFile);
                testSong.setDownloaded(true);
                testSong.setDurationSeconds(180); // 3 minutes default
                
                boolean loaded = loadSong(testSong);
                System.out.println("🎵 Test playback - Load: " + (loaded ? "✅" : "❌"));
                
                if (loaded) {
                    boolean played = play();
                    System.out.println("🎵 Test playback - Play: " + (played ? "✅" : "❌"));
                    
                    // Stop after a moment
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    boolean stopped = stop();
                    System.out.println("🎵 Test playback - Stop: " + (stopped ? "✅" : "❌"));
                    
                    return played && stopped;
                }
            } else {
                System.out.println("❌ No audio files found for testing");
            }
        } catch (Exception e) {
            System.err.println("❌ Test playback failed: " + e.getMessage());
        }
        
        return false;
    }
}
