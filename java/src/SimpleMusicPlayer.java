import com.musicplayer.model.Song;
import com.musicplayer.model.User;
import com.musicplayer.service.UserService;
import com.musicplayer.util.PythonBridge;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;


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
        System.out.println("MUSIC PLAYER");
        System.out.println("============");
        System.out.println();

        try {
            initializeApplication();
            authenticateUser();
            runMainLoop();
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    /**
     * Initialize application components
     */
    private static void initializeApplication() {
        
        // Initialize Python bridge
        pythonBridge = new PythonBridge();
        if (!pythonBridge.isInitialized()) {
            throw new RuntimeException("Failed to initialize Python backend");
        }
        
        // Initialize user service
        userService = new UserService();
        
        // Load existing music
        loadExistingMusic();
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
    
    // Figure out who's using the app
    private static void authenticateUser() {
        System.out.println("\nWho are you?");
        System.out.println("============");

        while (currentUser == null) {
            System.out.println("1. I have an account (Login)");
            System.out.println("2. I'm new here (Register)");
            System.out.println("3. Just let me try it (Guest)");
            System.out.print("Pick one: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice == 1) {
                    currentUser = handleLogin();
                } else if (choice == 2) {
                    currentUser = handleRegistration();
                } else if (choice == 3) {
                    currentUser = createGuestUser();
                } else {
                    System.out.println("That's not one of the options. Try 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Just type a number please.");
            } catch (Exception e) {
                System.out.println("Something went wrong. Let's try again.");
            }
        }

        System.out.println("Hey there, " + currentUser.getDisplayName() + "!");
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
            System.out.println(" Registration failed: " + e.getMessage());
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
            System.out.println("Failed to create guest user: " + e.getMessage());
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
    
    // Show the main menu options to user
    private static void showMainMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("=========================");
        System.out.println("1. Find and download music");

        String libraryText = musicLibrary.size() == 1 ? "song" : "songs";
        System.out.println("2. Browse your library (" + musicLibrary.size() + " " + libraryText + ")");

        System.out.println("3. Control playback");

        String queueText = playQueue.size() == 1 ? "song" : "songs";
        System.out.println("4. Manage queue (" + playQueue.size() + " " + queueText + ")");

        System.out.println("5. Quick Tamil hits");
        System.out.println("6. Your profile");
        System.out.println("7. View stats");
        System.out.println("0. Quit");
        System.out.println();
    }

    // Process user's menu selection
    private static void handleMenuChoice(int choice) {
        if (choice == 1) {
            handleSearchAndDownload();
        } else if (choice == 2) {
            handleBrowseLibrary();
        } else if (choice == 3) {
            handlePlaybackControls();
        } else if (choice == 4) {
            handleQueueManagement();
        } else if (choice == 5) {
            handleQuickPlayTamil();
        } else if (choice == 6) {
            handleUserProfile();
        } else if (choice == 7) {
            handleStatistics();
        } else if (choice == 0) {
            System.out.println("Goodbye!");
            running = false;
        } else {
            System.out.println("That's not a valid option. Try again.");
        }
    }
    
    // Let user search for and download music
    private static void handleSearchAndDownload() {
        System.out.println("\nSearch and Download Music");
        System.out.println("========================");

        System.out.print("What song would you like? ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {
            System.out.println("You need to tell me what to search for!");
            return;
        }

        downloadAndPlay(query);
    }
    
    /**
     * Download and play a song
     */
    private static void downloadAndPlay(String query) {
        System.out.println("Getting " + query + " for you...");
        System.out.println("This might take a minute or two, hang tight!");
        
        CompletableFuture.supplyAsync(() -> {
            try {
                // Download the song
                boolean downloadSuccess = pythonBridge.downloadSong(query);
                if (!downloadSuccess) {
                    System.out.println("Hmm, couldn't get that one. Try something else?");
                    return false;
                }

                // Look for the file we just downloaded
                String latestFile = pythonBridge.findLatestAudioFile();
                if (latestFile == null) {
                    System.out.println("Strange... downloaded but can't find the file");
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
                        
                        System.out.println("Playing: " + song.getDisplayName());

                        // Add it to our queue too
                        playQueue.offer(song);

                        return true;
                    } else {
                        System.out.println("Got the file but can't play it right now");
                        return false;
                    }
                } else {
                    System.out.println("Something went wrong processing the download");
                    return false;
                }
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return false;
            }
        }).thenAccept(success -> {
            if (success) {
                System.out.println("Download and playback completed successfully!");
            }
        });
    }

    /**
     * Handle library browsing
     */
    private static void handleBrowseLibrary() {
        System.out.println("\n MUSIC LIBRARY");
        System.out.println("================");

        if (musicLibrary.isEmpty()) {
            System.out.println("You don't have any music yet. Try downloading something first!");
            return;
        }

        System.out.println("Here's what you've got:");
        for (int i = 0; i < musicLibrary.size(); i++) {
            Song song = musicLibrary.get(i);
            String playText = song.getPlayCount() == 1 ? "play" : "plays";
            System.out.printf("%2d. %s (%d %s)\n",
                i + 1, song.getDisplayName(), song.getPlayCount(), playText);
        }

        System.out.print("\nWhich one? (0 to go back): ");
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
                System.out.println("Playing: " + song.getDisplayName());
            } else {
                System.out.println("Can't play that one right now");
            }
        } else {
            System.out.println("Hmm, can't find that file anymore");
        }
    }

    /**
     * Handle playback controls
     */
    private static void handlePlaybackControls() {
        System.out.println("\n PLAYBACK CONTROLS");
        System.out.println("====================");

        if (currentlyPlaying != null) {
            System.out.println(" Currently Playing: " + currentlyPlaying.getDisplayName());
        } else {
            System.out.println(" Nothing is currently playing");
        }

        System.out.println("\n1.  Play Next in Queue");
        System.out.println("2.  Shuffle Library");
        System.out.println("3.  Play Random Song");
        System.out.println("0.  Back");

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
            System.out.println(" Queue is empty");
        }
    }

    private static void shuffleAndPlay() {
        if (!musicLibrary.isEmpty()) {
            Collections.shuffle(musicLibrary);
            System.out.println(" Library shuffled!");
            playSong(musicLibrary.get(0));
        } else {
            System.out.println(" Library is empty");
        }
    }

    private static void playRandomSong() {
        if (!musicLibrary.isEmpty()) {
            Random random = new Random();
            Song randomSong = musicLibrary.get(random.nextInt(musicLibrary.size()));
            playSong(randomSong);
        } else {
            System.out.println(" Library is empty");
        }
    }

    /**
     * Handle queue management
     */
    private static void handleQueueManagement() {
        System.out.println("\n QUEUE MANAGEMENT");
        System.out.println("===================");

        if (playQueue.isEmpty()) {
            System.out.println(" Queue is empty");
            return;
        }

        System.out.println(" Current Queue:");
        List<Song> queueList = new ArrayList<>(playQueue);
        for (int i = 0; i < queueList.size(); i++) {
            System.out.println((i + 1) + ". " + queueList.get(i).getDisplayName());
        }

        System.out.println("\n1.  Play Next");
        System.out.println("2.  Clear Queue");
        System.out.println("0.  Back");

        int choice = getIntInput("Choose option: ");

        switch (choice) {
            case 1 -> playNextInQueue();
            case 2 -> {
                playQueue.clear();
                System.out.println(" Queue cleared");
            }
        }
    }

    /**
     * Handle quick play Tamil songs
     */
    private static void handleQuickPlayTamil() {
        System.out.println("\n QUICK PLAY TAMIL SONGS");
        System.out.println("=========================");

        String[] tamilSongs = {
            "Vaathi Coming", "Kutti Story", "Master the Blaster",
            "Anirudh Mashup", "Rowdy Baby", "Maari Thara Local",
            "Danga Maari", "Kaavaalaa", "Arabic Kuthu", "Beast Mode"
        };

        System.out.println("Some popular Tamil hits:");
        for (int i = 0; i < tamilSongs.length; i++) {
            System.out.println((i + 1) + ". " + tamilSongs[i]);
        }
        System.out.println((tamilSongs.length + 1) + ". Search for something else");
        System.out.println("0. Never mind, go back");

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
        System.out.println("\n USER PROFILE");
        System.out.println("===============");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Display Name: " + currentUser.getDisplayName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Recently Played: " + currentUser.getRecentlyPlayed().size() + " songs");

        if (!currentUser.getRecentlyPlayed().isEmpty()) {
            System.out.println("\nRECENTLY PLAYED:");
            List<Song> recent = currentUser.getRecentlyPlayed();
            for (int i = 0; i < Math.min(5, recent.size()); i++) {
                System.out.println("  " + recent.get(i).getDisplayName());
            }
        }
    }

    /**
     * Handle statistics
     */
    private static void handleStatistics() {
        System.out.println("\n MUSIC STATISTICS");
        System.out.println("===================");
        System.out.println(" User: " + currentUser.getDisplayName());
        System.out.println(" Total Songs: " + musicLibrary.size());
        System.out.println(" Session Downloads: " + sessionDownloads.size());
        System.out.println(" Recently Played: " + currentUser.getRecentlyPlayed().size());
        System.out.println(" Current Queue: " + playQueue.size() + " songs");

        if (currentlyPlaying != null) {
            System.out.println(" Now Playing: " + currentlyPlaying.getDisplayName());
        }

        // Show most played songs
        List<Song> sortedSongs = new ArrayList<>(musicLibrary);
        sortedSongs.sort((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()));

        if (!sortedSongs.isEmpty()) {
            System.out.println("\n TOP 5 MOST PLAYED:");
            for (int i = 0; i < Math.min(5, sortedSongs.size()); i++) {
                Song song = sortedSongs.get(i);
                System.out.println((i + 1) + ". " + song.getDisplayName() +
                                 " (" + song.getPlayCount() + " plays)");
            }
        }
    }

    // Get a number from user with some basic validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("That's not a number. Try again.");
            } catch (Exception e) {
                // Handle case where scanner runs out of input
                System.out.println("\nInput error occurred. Exiting...");
                running = false;
                return 0;
            }
        }
    }

    /**
     * Cleanup resources
     */
    private static void cleanup() {
        System.out.println("\nTidying up...");

        // Remove any downloads that weren't played
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
                        }
                    }
                }
            }

            if (deletedCount > 0) {
                System.out.println("Removed " + deletedCount + " temporary files");
            }
        }

        // Close Python connection
        if (pythonBridge != null) {
            pythonBridge.cleanup();
        }

        System.out.println("Thanks for listening!");
    }
}
