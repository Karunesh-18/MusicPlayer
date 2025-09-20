import com.musicplayer.model.Song;
import com.musicplayer.model.User;
import com.musicplayer.service.UserService;
import com.musicplayer.util.PythonBridge;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Simplified Spotify-like Music Player
 * Clean Java OOP implementation with real Python backend
 * Optimized for performance and efficiency
 */
public class SimpleMusicPlayer {
    // Core components
    private static final Scanner scanner = new Scanner(System.in);
    private static PythonBridge pythonBridge;
    private static UserService userService;
    private static User currentUser;
    
    // Music library
    private static final List<Song> musicLibrary = new ArrayList<>();
    private static final Queue<Song> playQueue = new LinkedList<>();
    private static Song currentlyPlaying = null;
    
    // Session management
    private static final List<String> sessionDownloads = new ArrayList<>();
    private static boolean running = true;
    
    public static void main(String[] args) {
        System.out.println("🎵 SIMPLIFIED SPOTIFY MUSIC PLAYER");
        System.out.println("==================================");
        System.out.println("🏗️ Java OOP | 🐍 Real Python Backend | 🎶 High Performance");
        System.out.println();
        
        try {
            initializeApplication();
            authenticateUser();
            runMainLoop();
        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    /**
     * Initialize application components
     */
    private static void initializeApplication() {
        System.out.println("🔧 Initializing application...");
        
        // Initialize Python bridge
        pythonBridge = new PythonBridge();
        if (!pythonBridge.isInitialized()) {
            throw new RuntimeException("Failed to initialize Python backend");
        }
        
        // Initialize user service
        userService = new UserService();
        
        // Load existing music
        loadExistingMusic();
        
        System.out.println("✅ Application initialized successfully");
        System.out.println("🐍 Python backend: Connected");
        System.out.println("📚 Library: " + musicLibrary.size() + " songs loaded");
    }
    
    /**
     * Load existing music from downloads directory
     */
    private static void loadExistingMusic() {
        File downloadsDir = new File("downloads");
        if (!downloadsDir.exists()) {
            return;
        }
        
        File[] audioFiles = downloadsDir.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".mp3") ||
            name.toLowerCase().endsWith(".wav") ||
            name.toLowerCase().endsWith(".m4a"));
        
        if (audioFiles != null) {
            for (File file : audioFiles) {
                Song song = createSongFromFile(file);
                if (song != null) {
                    musicLibrary.add(song);
                }
            }
        }
    }
    
    /**
     * Create a song object from a file
     */
    private static Song createSongFromFile(File file) {
        String fileName = file.getName();
        String[] parts = fileName.replace(".mp3", "").replace(".wav", "").replace(".m4a", "").split(" - ", 2);
        
        String artist = parts.length > 1 ? parts[0] : "Unknown Artist";
        String title = parts.length > 1 ? parts[1] : fileName.substring(0, fileName.lastIndexOf('.'));
        
        Song song = new Song(title, artist);
        song.setFilePath(file.getAbsolutePath());
        return song;
    }
    
    /**
     * Handle user authentication
     */
    private static void authenticateUser() {
        System.out.println("\n👤 USER AUTHENTICATION");
        System.out.println("======================");
        
        while (currentUser == null) {
            System.out.println("1. 🔑 Login");
            System.out.println("2. 📝 Register");
            System.out.println("3. 👤 Guest Mode");
            System.out.print("Choose option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> currentUser = handleLogin();
                    case 2 -> currentUser = handleRegistration();
                    case 3 -> currentUser = createGuestUser();
                    default -> System.out.println("❌ Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
        
        System.out.println("✅ Welcome, " + currentUser.getDisplayName() + "!");
    }
    
    private static User handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        return userService.authenticateUser(username, password).orElse(null);
    }
    
    private static User handleRegistration() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Display Name: ");
        String displayName = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        try {
            return userService.registerUser(username, email, displayName, password);
        } catch (Exception e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            return null;
        }
    }
    
    private static User createGuestUser() {
        try {
            return userService.registerUser("guest_" + System.currentTimeMillis(), 
                                          "guest@musicplayer.com", 
                                          "Guest User", 
                                          "guest123");
        } catch (Exception e) {
            System.out.println("❌ Failed to create guest user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Main application loop
     */
    private static void runMainLoop() {
        while (running) {
            showMainMenu();
            int choice = getIntInput("Choose option: ");
            handleMenuChoice(choice);
        }
    }
    
    /**
     * Display main menu
     */
    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        showStatus();
        System.out.println("=".repeat(50));
        
        System.out.println("\n🎵 MAIN MENU");
        System.out.println("============");
        System.out.println("1. 🔍 Search & Download Music");
        System.out.println("2. 📚 Browse Library (" + musicLibrary.size() + " songs)");
        System.out.println("3. ▶️ Playback Controls");
        System.out.println("4. 📋 Queue Management (" + playQueue.size() + " songs)");
        System.out.println("5. 🎯 Quick Play Tamil Songs");
        System.out.println("6. 👤 User Profile");
        System.out.println("7. 📊 Statistics");
        System.out.println("0. ❌ Exit");
        System.out.println();
    }
    
    /**
     * Show current status
     */
    private static void showStatus() {
        System.out.println("👤 User: " + currentUser.getDisplayName());
        
        if (currentlyPlaying != null) {
            System.out.println("🎶 Now Playing: " + currentlyPlaying.getDisplayName());
        }
        
        System.out.println("📚 Library: " + musicLibrary.size() + " songs");
        System.out.println("🐍 Backend: ✅ Connected");
    }
    
    /**
     * Handle menu choices
     */
    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleSearchAndDownload();
            case 2 -> handleBrowseLibrary();
            case 3 -> handlePlaybackControls();
            case 4 -> handleQueueManagement();
            case 5 -> handleQuickPlayTamil();
            case 6 -> handleUserProfile();
            case 7 -> handleStatistics();
            case 0 -> {
                System.out.println("👋 Goodbye!");
                running = false;
            }
            default -> System.out.println("❌ Invalid option. Please try again.");
        }
    }
    
    /**
     * Handle search and download
     */
    private static void handleSearchAndDownload() {
        System.out.println("\n🔍 SEARCH & DOWNLOAD");
        System.out.println("====================");
        
        System.out.print("Enter song name or artist: ");
        String query = scanner.nextLine().trim();
        
        if (query.isEmpty()) {
            System.out.println("❌ Please enter a search term");
            return;
        }
        
        downloadAndPlay(query);
    }
    
    /**
     * Download and play a song
     */
    private static void downloadAndPlay(String query) {
        System.out.println("⬇️ Downloading: " + query);
        System.out.println("⏳ Please wait (30-90 seconds)...");
        
        CompletableFuture.supplyAsync(() -> {
            try {
                // Download the song
                boolean downloadSuccess = pythonBridge.downloadSong(query);
                if (!downloadSuccess) {
                    System.out.println("❌ Download failed");
                    return false;
                }
                
                // Find the downloaded file
                String latestFile = pythonBridge.findLatestAudioFile();
                if (latestFile == null) {
                    System.out.println("❌ Downloaded file not found");
                    return false;
                }
                
                // Create song object and add to library
                File file = new File(latestFile);
                Song song = createSongFromFile(file);
                if (song != null) {
                    musicLibrary.add(song);
                    sessionDownloads.add(latestFile);
                    
                    // Play the song
                    boolean playSuccess = pythonBridge.playAudioFile(latestFile);
                    if (playSuccess) {
                        currentlyPlaying = song;
                        song.play(); // Increment play count
                        currentUser.addToRecentlyPlayed(song);
                        
                        System.out.println("✅ Now playing: " + song.getDisplayName());
                        
                        // Add to queue for continuous play
                        playQueue.offer(song);
                        
                        return true;
                    } else {
                        System.out.println("❌ Playback failed");
                        return false;
                    }
                } else {
                    System.out.println("❌ Failed to create song object");
                    return false;
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                return false;
            }
        }).thenAccept(success -> {
            if (success) {
                System.out.println("🎉 Download and playback completed successfully!");
            }
        });
    }

    /**
     * Handle library browsing
     */
    private static void handleBrowseLibrary() {
        System.out.println("\n📚 MUSIC LIBRARY");
        System.out.println("================");

        if (musicLibrary.isEmpty()) {
            System.out.println("📭 Your library is empty. Download some music first!");
            return;
        }

        System.out.println("🎵 Your Music Library:");
        for (int i = 0; i < musicLibrary.size(); i++) {
            Song song = musicLibrary.get(i);
            System.out.printf("%2d. %s (♪ %d plays)\n",
                i + 1, song.getDisplayName(), song.getPlayCount());
        }

        System.out.print("\nEnter song number to play (0 to go back): ");
        int choice = getIntInput("");

        if (choice > 0 && choice <= musicLibrary.size()) {
            Song selectedSong = musicLibrary.get(choice - 1);
            playSong(selectedSong);
        }
    }

    /**
     * Play a song
     */
    private static void playSong(Song song) {
        if (song.getFilePath() != null) {
            boolean success = pythonBridge.playAudioFile(song.getFilePath());
            if (success) {
                currentlyPlaying = song;
                song.play();
                currentUser.addToRecentlyPlayed(song);
                System.out.println("▶️ Now playing: " + song.getDisplayName());
            } else {
                System.out.println("❌ Failed to play song");
            }
        } else {
            System.out.println("❌ Song file not found");
        }
    }

    /**
     * Handle playback controls
     */
    private static void handlePlaybackControls() {
        System.out.println("\n▶️ PLAYBACK CONTROLS");
        System.out.println("====================");

        if (currentlyPlaying != null) {
            System.out.println("🎶 Currently Playing: " + currentlyPlaying.getDisplayName());
        } else {
            System.out.println("⏸️ Nothing is currently playing");
        }

        System.out.println("\n1. ⏭️ Play Next in Queue");
        System.out.println("2. 🔀 Shuffle Library");
        System.out.println("3. 🎲 Play Random Song");
        System.out.println("0. 🔙 Back");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1 -> playNextInQueue();
            case 2 -> shuffleAndPlay();
            case 3 -> playRandomSong();
        }
    }

    private static void playNextInQueue() {
        if (!playQueue.isEmpty()) {
            Song nextSong = playQueue.poll();
            playSong(nextSong);
        } else {
            System.out.println("📭 Queue is empty");
        }
    }

    private static void shuffleAndPlay() {
        if (!musicLibrary.isEmpty()) {
            Collections.shuffle(musicLibrary);
            System.out.println("🔀 Library shuffled!");
            playSong(musicLibrary.get(0));
        } else {
            System.out.println("📭 Library is empty");
        }
    }

    private static void playRandomSong() {
        if (!musicLibrary.isEmpty()) {
            Random random = new Random();
            Song randomSong = musicLibrary.get(random.nextInt(musicLibrary.size()));
            playSong(randomSong);
        } else {
            System.out.println("📭 Library is empty");
        }
    }

    /**
     * Handle queue management
     */
    private static void handleQueueManagement() {
        System.out.println("\n📋 QUEUE MANAGEMENT");
        System.out.println("===================");

        if (playQueue.isEmpty()) {
            System.out.println("📭 Queue is empty");
            return;
        }

        System.out.println("📋 Current Queue:");
        List<Song> queueList = new ArrayList<>(playQueue);
        for (int i = 0; i < queueList.size(); i++) {
            System.out.println((i + 1) + ". " + queueList.get(i).getDisplayName());
        }

        System.out.println("\n1. ▶️ Play Next");
        System.out.println("2. 🗑️ Clear Queue");
        System.out.println("0. 🔙 Back");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1 -> playNextInQueue();
            case 2 -> {
                playQueue.clear();
                System.out.println("🗑️ Queue cleared");
            }
        }
    }

    /**
     * Handle quick play Tamil songs
     */
    private static void handleQuickPlayTamil() {
        System.out.println("\n🎯 QUICK PLAY TAMIL SONGS");
        System.out.println("=========================");

        String[] tamilSongs = {
            "Vaathi Coming", "Kutti Story", "Master the Blaster",
            "Anirudh Mashup", "Rowdy Baby", "Maari Thara Local",
            "Danga Maari", "Kaavaalaa", "Arabic Kuthu", "Beast Mode"
        };

        System.out.println("🎵 Popular Tamil Songs:");
        for (int i = 0; i < tamilSongs.length; i++) {
            System.out.println((i + 1) + ". " + tamilSongs[i]);
        }
        System.out.println((tamilSongs.length + 1) + ". Custom Search");
        System.out.println("0. 🔙 Back");

        int choice = getIntInput("Choose song: ");

        if (choice > 0 && choice <= tamilSongs.length) {
            downloadAndPlay(tamilSongs[choice - 1]);
        } else if (choice == tamilSongs.length + 1) {
            System.out.print("Enter Tamil song name: ");
            String customSong = scanner.nextLine().trim();
            if (!customSong.isEmpty()) {
                downloadAndPlay(customSong);
            }
        }
    }

    /**
     * Handle user profile
     */
    private static void handleUserProfile() {
        System.out.println("\n👤 USER PROFILE");
        System.out.println("===============");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Display Name: " + currentUser.getDisplayName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Recently Played: " + currentUser.getRecentlyPlayed().size() + " songs");

        if (!currentUser.getRecentlyPlayed().isEmpty()) {
            System.out.println("\n🕒 RECENTLY PLAYED:");
            List<Song> recent = currentUser.getRecentlyPlayed();
            for (int i = 0; i < Math.min(5, recent.size()); i++) {
                System.out.println("  ♪ " + recent.get(i).getDisplayName());
            }
        }
    }

    /**
     * Handle statistics
     */
    private static void handleStatistics() {
        System.out.println("\n📊 MUSIC STATISTICS");
        System.out.println("===================");
        System.out.println("👤 User: " + currentUser.getDisplayName());
        System.out.println("🎵 Total Songs: " + musicLibrary.size());
        System.out.println("📥 Session Downloads: " + sessionDownloads.size());
        System.out.println("🕒 Recently Played: " + currentUser.getRecentlyPlayed().size());
        System.out.println("📋 Current Queue: " + playQueue.size() + " songs");

        if (currentlyPlaying != null) {
            System.out.println("🎶 Now Playing: " + currentlyPlaying.getDisplayName());
        }

        // Show most played songs
        List<Song> sortedSongs = new ArrayList<>(musicLibrary);
        sortedSongs.sort((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()));

        if (!sortedSongs.isEmpty()) {
            System.out.println("\n🔥 TOP 5 MOST PLAYED:");
            for (int i = 0; i < Math.min(5, sortedSongs.size()); i++) {
                Song song = sortedSongs.get(i);
                System.out.println((i + 1) + ". " + song.getDisplayName() +
                                 " (" + song.getPlayCount() + " plays)");
            }
        }
    }

    /**
     * Get integer input with validation
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    /**
     * Cleanup resources
     */
    private static void cleanup() {
        System.out.println("\n🧹 CLEANING UP SESSION...");

        // Smart cleanup - delete unused downloads
        if (!sessionDownloads.isEmpty()) {
            int deletedCount = 0;
            for (String filePath : sessionDownloads) {
                File file = new File(filePath);
                if (file.exists()) {
                    // Check if song was played
                    boolean wasPlayed = musicLibrary.stream()
                        .anyMatch(song -> filePath.equals(song.getFilePath()) && song.getPlayCount() > 0);

                    if (!wasPlayed) {
                        if (file.delete()) {
                            deletedCount++;
                            System.out.println("🗑️ Deleted unused download: " + file.getName());
                        }
                    }
                }
            }

            if (deletedCount > 0) {
                System.out.println("✅ Cleaned up " + deletedCount + " unused downloads");
            } else {
                System.out.println("✅ No cleanup needed - all downloads were used");
            }
        }

        // Cleanup Python bridge
        if (pythonBridge != null) {
            pythonBridge.cleanup();
        }

        System.out.println("👋 Thank you for using Simplified Spotify Music Player!");
    }
}
