package com.musicplayer;

import com.musicplayer.model.*;
import com.musicplayer.service.*;
import com.musicplayer.player.*;
import com.musicplayer.repository.*;
import com.musicplayer.ui.*;
import com.musicplayer.util.*;
import java.util.Map;

/**
 * Main application class for the Spotify-like Music Player.
 * Implements dependency injection pattern and manages application lifecycle.
 */
public class MusicPlayerApplication {
    
    // Core components
    private PythonBridge pythonBridge;
    private JsonSongRepository songRepository;
    private JsonUserRepository userRepository;
    private MusicLibraryService musicLibraryService;
    private PlaylistService playlistService;
    private UserService userService;
    private DownloadService downloadService;
    private SocialService socialService;
    private AudioPlayer audioPlayer;
    private PlaybackController playbackController;
    private MusicPlayerUI userInterface;
    
    // Application state
    private boolean initialized;
    private boolean running;
    
    public MusicPlayerApplication() {
        this.initialized = false;
        this.running = false;
    }
    
    /**
     * Initialize all application components with dependency injection.
     */
    public boolean initialize() {
        if (initialized) {
            return true;
        }
        
        System.out.println("🚀 Initializing Spotify-like Music Player...");
        
        try {
            // Initialize Python Bridge
            System.out.println("🐍 Initializing Python Bridge...");
            pythonBridge = new PythonBridge();
            if (!pythonBridge.isInitialized()) {
                System.err.println("❌ Failed to initialize Python Bridge");
                return false;
            }
            
            // Test Python Bridge
            if (!pythonBridge.testBridge()) {
                System.err.println("⚠️ Python Bridge test failed - some features may not work");
            }
            
            // Initialize Repositories
            System.out.println("💾 Initializing Data Repositories...");
            songRepository = new JsonSongRepository();
            userRepository = new JsonUserRepository();
            
            // Initialize Services
            System.out.println("⚙️ Initializing Services...");
            musicLibraryService = new MusicLibraryService(songRepository);
            playlistService = new PlaylistService();
            userService = new UserService(userRepository);
            downloadService = new DownloadService(pythonBridge, musicLibraryService);
            socialService = new SocialService(userRepository, playlistService);
            
            // Initialize Audio Player
            System.out.println("🎵 Initializing Audio Player...");
            audioPlayer = new LocalAudioPlayer(pythonBridge);
            if (!audioPlayer.isSupported()) {
                System.err.println("❌ Audio Player not supported");
                return false;
            }
            
            // Initialize Playback Controller
            System.out.println("🎛️ Initializing Playback Controller...");
            playbackController = new PlaybackController(audioPlayer);
            
            // Initialize User Interface
            System.out.println("🖥️ Initializing User Interface...");
            userInterface = new MusicPlayerUI(
                musicLibraryService,
                playlistService,
                userService,
                downloadService,
                playbackController
            );
            
            // Setup event listeners
            setupEventListeners();
            
            initialized = true;
            System.out.println("✅ Music Player initialized successfully!");
            
            // Show initialization statistics
            showInitializationStats();
            
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Music Player: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Start the music player application.
     */
    public void start() {
        if (!initialized) {
            if (!initialize()) {
                System.err.println("❌ Cannot start - initialization failed");
                return;
            }
        }
        
        if (running) {
            System.out.println("⚠️ Application is already running");
            return;
        }
        
        running = true;
        System.out.println("🎵 Starting Music Player Application...");
        
        try {
            // Start the user interface
            userInterface.start();
            
        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            running = false;
            shutdown();
        }
    }
    
    /**
     * Stop the music player application.
     */
    public void stop() {
        if (!running) {
            return;
        }
        
        System.out.println("🛑 Stopping Music Player Application...");
        running = false;
        
        if (userInterface != null) {
            userInterface.stop();
        }
    }
    
    /**
     * Shutdown and cleanup all resources.
     */
    public void shutdown() {
        System.out.println("🧹 Shutting down Music Player Application...");
        
        try {
            // Stop playback
            if (playbackController != null) {
                playbackController.cleanup();
            }
            
            // Cleanup audio player
            if (audioPlayer != null) {
                audioPlayer.cleanup();
            }
            
            // Flush repositories
            if (songRepository != null) {
                songRepository.flush();
            }
            if (userRepository != null) {
                userRepository.flush();
            }
            
            // Cleanup Python bridge
            if (pythonBridge != null) {
                pythonBridge.cleanup();
            }
            
            System.out.println("✅ Shutdown complete");
            
        } catch (Exception e) {
            System.err.println("❌ Error during shutdown: " + e.getMessage());
        }
    }
    
    /**
     * Setup event listeners for cross-component communication.
     */
    private void setupEventListeners() {
        // Download service listener
        downloadService.addDownloadListener(new DownloadService.DownloadListener() {
            @Override
            public void onDownloadStarted(DownloadService.DownloadTask task) {
                System.out.println("⏳ Download started: " + task.getQuery());
            }

            @Override
            public void onDownloadProgress(DownloadService.DownloadTask task, double progress) {
                // Progress updates handled by UI
            }

            @Override
            public void onDownloadCompleted(DownloadService.DownloadTask task, Song song) {
                if (song != null) {
                    System.out.println("✅ Download completed: " + song.getDisplayName());

                    // Add to user's recently played if there's a current user
                    if (playbackController.getCurrentUser() != null) {
                        playbackController.getCurrentUser().addToRecentlyPlayed(song);
                    }
                }
            }

            @Override
            public void onDownloadFailed(DownloadService.DownloadTask task, String error) {
                System.err.println("❌ Download failed: " + task.getQuery() + " - " + error);
            }

            @Override
            public void onDownloadCancelled(DownloadService.DownloadTask task) {
                System.out.println("🚫 Download cancelled: " + task.getQuery());
            }
        });
        
        // Social service listener
        socialService.addSocialEventListener(new SocialService.SocialEventListener() {
            @Override
            public void onSocialEvent(SocialService.SocialEventType eventType, String userId, 
                                    String targetId, Map<String, Object> data) {
                // Log social events
                switch (eventType) {
                    case USER_FOLLOWED -> System.out.println("👥 Social: User followed");
                    case PLAYLIST_SHARED -> System.out.println("📋 Social: Playlist shared");
                    case PLAYLIST_LIKED -> System.out.println("❤️ Social: Playlist liked");
                    default -> System.out.println("📱 Social: " + eventType);
                }
            }
        });
        
        // Playback controller listener
        playbackController.addListener(new PlaybackController.PlaybackControllerListener() {
            @Override
            public void onQueueChanged(java.util.List<Song> newQueue) {
                System.out.println("📋 Queue updated: " + newQueue.size() + " songs");
            }
            
            @Override
            public void onShuffleChanged(boolean shuffleEnabled) {
                System.out.println("🔀 Shuffle " + (shuffleEnabled ? "enabled" : "disabled"));
            }
            
            @Override
            public void onRepeatModeChanged(PlaybackController.RepeatMode repeatMode) {
                System.out.println("🔁 Repeat mode: " + repeatMode);
            }
            
            @Override
            public void onTrackChanged(Song previousTrack, Song currentTrack) {
                if (currentTrack != null) {
                    System.out.println("🎵 Now playing: " + currentTrack.getDisplayName());
                }
            }
            
            @Override
            public void onQueueEnded() {
                System.out.println("🎵 Queue ended");
            }
        });
    }
    
    /**
     * Show initialization statistics.
     */
    private void showInitializationStats() {
        System.out.println("\n📊 INITIALIZATION STATISTICS");
        System.out.println("-".repeat(40));
        
        // Repository stats
        System.out.println("💾 Songs in library: " + songRepository.count());
        System.out.println("👤 Registered users: " + userRepository.count());
        
        // Python bridge stats
        Map<String, Object> bridgeStatus = pythonBridge.getStatus();
        System.out.println("🐍 Python bridge: " + (bridgeStatus.get("initialized") + "").toUpperCase());
        System.out.println("🎵 Audio files: " + bridgeStatus.get("audioFileCount"));
        
        // Cache stats
        Map<String, Object> cacheStats = pythonBridge.getCacheStats();
        System.out.println("💾 Cache entries: " + cacheStats.get("cacheSize"));
        
        System.out.println("-".repeat(40));
    }
    
    /**
     * Get application status information.
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new java.util.HashMap<>();
        
        status.put("initialized", initialized);
        status.put("running", running);
        status.put("pythonBridgeStatus", pythonBridge != null ? pythonBridge.getStatus() : null);
        status.put("songCount", songRepository != null ? songRepository.count() : 0);
        status.put("userCount", userRepository != null ? userRepository.count() : 0);
        status.put("audioPlayerSupported", audioPlayer != null ? audioPlayer.isSupported() : false);
        
        return status;
    }
    
    /**
     * Perform health check on all components.
     */
    public boolean healthCheck() {
        System.out.println("🏥 Performing health check...");
        
        boolean healthy = true;
        
        // Check Python bridge
        if (pythonBridge == null || !pythonBridge.isInitialized()) {
            System.err.println("❌ Python bridge not healthy");
            healthy = false;
        } else {
            System.out.println("✅ Python bridge healthy");
        }
        
        // Check repositories
        try {
            songRepository.count();
            userRepository.count();
            System.out.println("✅ Repositories healthy");
        } catch (Exception e) {
            System.err.println("❌ Repository error: " + e.getMessage());
            healthy = false;
        }
        
        // Check audio player
        if (audioPlayer == null || !audioPlayer.isSupported()) {
            System.err.println("❌ Audio player not healthy");
            healthy = false;
        } else {
            System.out.println("✅ Audio player healthy");
        }
        
        System.out.println("🏥 Health check " + (healthy ? "PASSED" : "FAILED"));
        return healthy;
    }
    
    /**
     * Create a default admin user for testing.
     */
    public User createDefaultUser() {
        try {
            User adminUser = userService.registerUser("admin", "admin@musicplayer.com", "Administrator", "admin123");
            adminUser.setSubscriptionType(User.SubscriptionType.PREMIUM);
            adminUser.setVerified(true);
            userRepository.save(adminUser);
            
            System.out.println("👤 Created default admin user: admin/admin123");
            return adminUser;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to create default user: " + e.getMessage());
            return null;
        }
    }
    
    // Getters for testing and external access
    public PythonBridge getPythonBridge() { return pythonBridge; }
    public MusicLibraryService getMusicLibraryService() { return musicLibraryService; }
    public PlaylistService getPlaylistService() { return playlistService; }
    public UserService getUserService() { return userService; }
    public DownloadService getDownloadService() { return downloadService; }
    public SocialService getSocialService() { return socialService; }
    public PlaybackController getPlaybackController() { return playbackController; }
    public boolean isInitialized() { return initialized; }
    public boolean isRunning() { return running; }
}
