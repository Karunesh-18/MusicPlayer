package com.musicplayer.ui;

import com.musicplayer.model.*;
import com.musicplayer.service.*;
import com.musicplayer.player.*;
import com.musicplayer.repository.*;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

/**
 * Interactive console-based user interface for the Music Player application.
 * Provides a comprehensive menu system for all music player functionalities.
 */
public class MusicPlayerUI {
    
    private final Scanner scanner;
    private final MusicLibraryService musicLibraryService;
    private final PlaylistService playlistService;
    private final UserService userService;
    private final DownloadService downloadService;
    private final PlaybackController playbackController;
    private User currentUser;
    private boolean running;
    
    public MusicPlayerUI(MusicLibraryService musicLibraryService,
                        PlaylistService playlistService,
                        UserService userService,
                        DownloadService downloadService,
                        PlaybackController playbackController) {
        this.scanner = new Scanner(System.in);
        this.musicLibraryService = musicLibraryService;
        this.playlistService = playlistService;
        this.userService = userService;
        this.downloadService = downloadService;
        this.playbackController = playbackController;
        this.running = false;
    }
    
    /**
     * Start the interactive user interface.
     */
    public void start() {
        running = true;
        showWelcome();
        
        // Login or register
        if (!handleAuthentication()) {
            System.out.println("👋 Goodbye!");
            return;
        }
        
        // Main application loop
        while (running) {
            try {
                showMainMenu();
                handleMainMenuChoice();
            } catch (Exception e) {
                System.err.println("❌ An error occurred: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
        
        cleanup();
    }
    
    /**
     * Stop the user interface.
     */
    public void stop() {
        running = false;
    }
    
    private void showWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎵 WELCOME TO SPOTIFY-LIKE MUSIC PLAYER 🎵");
        System.out.println("=".repeat(60));
        System.out.println("Your ultimate music streaming experience!");
        System.out.println();
    }
    
    private boolean handleAuthentication() {
        while (true) {
            System.out.println("🔐 Authentication Required");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> {
                    if (handleLogin()) return true;
                }
                case "2" -> {
                    if (handleRegistration()) return true;
                }
                case "3" -> {
                    return false;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private boolean handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        Optional<User> userOpt = userService.authenticateUser(username, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            playbackController.setCurrentUser(currentUser);
            System.out.println("✅ Welcome back, " + currentUser.getDisplayName() + "!");
            return true;
        } else {
            System.out.println("❌ Invalid credentials. Please try again.");
            return false;
        }
    }
    
    private boolean handleRegistration() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Display Name: ");
        String displayName = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        try {
            User newUser = userService.registerUser(username, email, displayName, password);
            currentUser = newUser;
            playbackController.setCurrentUser(currentUser);
            System.out.println("✅ Registration successful! Welcome, " + displayName + "!");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎵 MAIN MENU - " + currentUser.getDisplayName());
        System.out.println("=".repeat(50));
        
        // Show current playback status
        showPlaybackStatus();
        
        System.out.println("\n📚 LIBRARY & SEARCH");
        System.out.println("1. Search & Download Music");
        System.out.println("2. Browse Library");
        System.out.println("3. View Downloaded Songs");
        
        System.out.println("\n🎵 PLAYBACK");
        System.out.println("4. Play/Pause");
        System.out.println("5. Next Track");
        System.out.println("6. Previous Track");
        System.out.println("7. Playback Controls");
        
        System.out.println("\n📋 PLAYLISTS");
        System.out.println("8. My Playlists");
        System.out.println("9. Create Playlist");
        System.out.println("10. Browse Public Playlists");
        
        System.out.println("\n👤 SOCIAL");
        System.out.println("11. My Profile");
        System.out.println("12. Find Users");
        System.out.println("13. Following & Followers");
        
        System.out.println("\n⚙️ SETTINGS");
        System.out.println("14. User Settings");
        System.out.println("15. Audio Settings");
        
        System.out.println("\n16. Exit");
        System.out.print("\nChoose an option: ");
    }
    
    private void showPlaybackStatus() {
        Song currentSong = playbackController.getAudioPlayer().getCurrentSong();
        AudioPlayer.PlaybackState state = playbackController.getAudioPlayer().getState();
        
        if (currentSong != null) {
            String stateIcon = switch (state) {
                case PLAYING -> "▶️";
                case PAUSED -> "⏸️";
                case STOPPED -> "⏹️";
                case BUFFERING -> "⏳";
                case ERROR -> "❌";
            };
            
            System.out.println("🎵 Now: " + stateIcon + " " + currentSong.getDisplayName());
            System.out.println("📊 Queue: " + playbackController.getQueueSize() + " songs | " +
                             "🔀 Shuffle: " + (playbackController.isShuffleEnabled() ? "ON" : "OFF") + " | " +
                             "🔁 Repeat: " + playbackController.getRepeatMode());
        } else {
            System.out.println("🎵 No song currently loaded");
        }
    }
    
    private void handleMainMenuChoice() {
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> handleSearchAndDownload();
            case "2" -> handleBrowseLibrary();
            case "3" -> handleViewDownloaded();
            case "4" -> handlePlayPause();
            case "5" -> handleNextTrack();
            case "6" -> handlePreviousTrack();
            case "7" -> handlePlaybackControls();
            case "8" -> handleMyPlaylists();
            case "9" -> handleCreatePlaylist();
            case "10" -> handleBrowsePublicPlaylists();
            case "11" -> handleMyProfile();
            case "12" -> handleFindUsers();
            case "13" -> handleFollowingFollowers();
            case "14" -> handleUserSettings();
            case "15" -> handleAudioSettings();
            case "16" -> {
                System.out.println("👋 Goodbye, " + currentUser.getDisplayName() + "!");
                running = false;
            }
            default -> System.out.println("❌ Invalid choice. Please try again.");
        }
    }
    
    private void handleSearchAndDownload() {
        System.out.println("\n🔍 SEARCH & DOWNLOAD");
        System.out.print("Enter song name or artist: ");
        String query = scanner.nextLine().trim();
        
        if (query.isEmpty()) {
            System.out.println("❌ Please enter a search query.");
            return;
        }
        
        System.out.println("🔍 Searching for: " + query);
        
        // Start download
        DownloadService.DownloadTask task = downloadService.queueDownload(query, currentUser);
        
        if (task != null) {
            System.out.println("⏳ Download started... Please wait.");
            
            // Simple progress monitoring
            while (!task.isCompleted() && !task.isFailed()) {
                try {
                    Thread.sleep(1000);
                    System.out.print(".");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            System.out.println();
            
            if (task.isCompleted()) {
                Song downloadedSong = task.getSong();
                System.out.println("✅ Downloaded: " + downloadedSong.getDisplayName());
                
                // Ask if user wants to play it now
                System.out.print("Play now? (y/n): ");
                String playChoice = scanner.nextLine().trim().toLowerCase();
                if (playChoice.equals("y") || playChoice.equals("yes")) {
                    playbackController.getAudioPlayer().loadSong(downloadedSong);
                    playbackController.play();
                }
            } else {
                System.out.println("❌ Download failed: " + task.getErrorMessage());
            }
        } else {
            System.out.println("❌ Failed to start download.");
        }
    }
    
    private void handleBrowseLibrary() {
        System.out.println("\n📚 BROWSE LIBRARY");
        System.out.println("1. All Songs");
        System.out.println("2. By Artist");
        System.out.println("3. By Genre");
        System.out.println("4. Most Played");
        System.out.println("5. Recently Added");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> showAllSongs();
            case "2" -> browseByArtist();
            case "3" -> browseByGenre();
            case "4" -> showMostPlayed();
            case "5" -> showRecentlyAdded();
            default -> System.out.println("❌ Invalid choice.");
        }
    }
    
    private void showAllSongs() {
        List<Song> songs = musicLibraryService.getAllSongs();
        displaySongList(songs, "All Songs");
    }
    
    private void browseByArtist() {
        List<String> artists = musicLibraryService.getAllArtists().stream()
                .map(Artist::getName)
                .collect(java.util.stream.Collectors.toList());
        if (artists.isEmpty()) {
            System.out.println("📭 No artists found in your library.");
            return;
        }
        
        System.out.println("\n🎤 ARTISTS:");
        for (int i = 0; i < artists.size(); i++) {
            System.out.println((i + 1) + ". " + artists.get(i));
        }
        
        System.out.print("Select artist (number): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice >= 0 && choice < artists.size()) {
                String artist = artists.get(choice);
                List<Song> songs = musicLibraryService.searchSongs(artist);
                displaySongList(songs, "Songs by " + artist);
            } else {
                System.out.println("❌ Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
        }
    }
    
    private void browseByGenre() {
        List<String> genres = musicLibraryService.getAllGenres();
        if (genres.isEmpty()) {
            System.out.println("📭 No genres found in your library.");
            return;
        }
        
        System.out.println("\n🎼 GENRES:");
        for (int i = 0; i < genres.size(); i++) {
            System.out.println((i + 1) + ". " + genres.get(i));
        }
        
        System.out.print("Select genre (number): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice >= 0 && choice < genres.size()) {
                String genre = genres.get(choice);
                List<Song> songs = musicLibraryService.getSongsByGenre(genre);
                displaySongList(songs, "Songs in " + genre);
            } else {
                System.out.println("❌ Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
        }
    }
    
    private void showMostPlayed() {
        List<Song> songs = musicLibraryService.getMostPlayedSongs(20);
        displaySongList(songs, "Most Played Songs");
    }
    
    private void showRecentlyAdded() {
        List<Song> songs = musicLibraryService.getRecentlyAddedSongs(20);
        displaySongList(songs, "Recently Added Songs");
    }
    
    private void displaySongList(List<Song> songs, String title) {
        if (songs.isEmpty()) {
            System.out.println("📭 No songs found.");
            return;
        }
        
        System.out.println("\n🎵 " + title.toUpperCase());
        System.out.println("-".repeat(60));
        
        for (int i = 0; i < Math.min(songs.size(), 20); i++) {
            Song song = songs.get(i);
            String status = song.isDownloaded() ? "✅" : "⏳";
            String liked = song.isLiked() ? "❤️" : "";
            System.out.printf("%2d. %s %s %s (Plays: %d)%n", 
                            i + 1, status, song.getDisplayName(), liked, song.getPlayCount());
        }
        
        if (songs.size() > 20) {
            System.out.println("... and " + (songs.size() - 20) + " more songs");
        }
        
        System.out.print("\nSelect song to play (number) or press Enter to go back: ");
        String choice = scanner.nextLine().trim();
        
        if (!choice.isEmpty()) {
            try {
                int songIndex = Integer.parseInt(choice) - 1;
                if (songIndex >= 0 && songIndex < songs.size()) {
                    Song selectedSong = songs.get(songIndex);
                    if (selectedSong.isDownloaded()) {
                        playbackController.getAudioPlayer().loadSong(selectedSong);
                        playbackController.play();
                        System.out.println("▶️ Playing: " + selectedSong.getDisplayName());
                    } else {
                        System.out.println("❌ Song not downloaded yet.");
                    }
                } else {
                    System.out.println("❌ Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    private void handleViewDownloaded() {
        List<Song> downloadedSongs = musicLibraryService.getDownloadedSongs();
        displaySongList(downloadedSongs, "Downloaded Songs");
    }
    
    private void handlePlayPause() {
        if (playbackController.togglePlayPause()) {
            AudioPlayer.PlaybackState state = playbackController.getAudioPlayer().getState();
            String action = (state == AudioPlayer.PlaybackState.PLAYING) ? "▶️ Playing" : "⏸️ Paused";
            System.out.println(action);
        } else {
            System.out.println("❌ No song loaded or playback error.");
        }
    }
    
    private void handleNextTrack() {
        if (playbackController.playNext()) {
            Song currentSong = playbackController.getAudioPlayer().getCurrentSong();
            System.out.println("⏭️ Next: " + (currentSong != null ? currentSong.getDisplayName() : "Unknown"));
        } else {
            System.out.println("❌ No next track available.");
        }
    }
    
    private void handlePreviousTrack() {
        if (playbackController.playPrevious()) {
            Song currentSong = playbackController.getAudioPlayer().getCurrentSong();
            System.out.println("⏮️ Previous: " + (currentSong != null ? currentSong.getDisplayName() : "Unknown"));
        } else {
            System.out.println("❌ No previous track available.");
        }
    }
    
    private void handlePlaybackControls() {
        System.out.println("\n🎛️ PLAYBACK CONTROLS");
        System.out.println("1. Toggle Shuffle (" + (playbackController.isShuffleEnabled() ? "ON" : "OFF") + ")");
        System.out.println("2. Cycle Repeat Mode (" + playbackController.getRepeatMode() + ")");
        System.out.println("3. Volume Control");
        System.out.println("4. View Queue");
        System.out.println("5. Clear Queue");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> {
                playbackController.toggleShuffle();
                System.out.println("🔀 Shuffle " + (playbackController.isShuffleEnabled() ? "enabled" : "disabled"));
            }
            case "2" -> {
                PlaybackController.RepeatMode mode = playbackController.cycleRepeatMode();
                System.out.println("🔁 Repeat mode: " + mode);
            }
            case "3" -> handleVolumeControl();
            case "4" -> handleViewQueue();
            case "5" -> {
                playbackController.clearQueue();
                System.out.println("🗑️ Queue cleared");
            }
            default -> System.out.println("❌ Invalid choice.");
        }
    }
    
    private void handleVolumeControl() {
        AudioPlayer audioPlayer = playbackController.getAudioPlayer();
        System.out.println("🔊 Current volume: " + (int)(audioPlayer.getVolume() * 100) + "%");
        System.out.print("Enter new volume (0-100): ");
        
        try {
            int volume = Integer.parseInt(scanner.nextLine().trim());
            if (volume >= 0 && volume <= 100) {
                audioPlayer.setVolume(volume / 100.0);
                System.out.println("🔊 Volume set to " + volume + "%");
            } else {
                System.out.println("❌ Volume must be between 0 and 100.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
        }
    }
    
    private void handleViewQueue() {
        List<Song> queue = playbackController.getQueue();
        if (queue.isEmpty()) {
            System.out.println("📭 Queue is empty.");
            return;
        }
        
        System.out.println("\n📋 CURRENT QUEUE");
        System.out.println("-".repeat(50));
        
        for (int i = 0; i < queue.size(); i++) {
            Song song = queue.get(i);
            System.out.printf("%2d. %s%n", i + 1, song.getDisplayName());
        }
    }
    
    // Placeholder methods for remaining functionality
    private void handleMyPlaylists() {
        System.out.println("🚧 My Playlists - Coming soon!");
    }
    
    private void handleCreatePlaylist() {
        System.out.println("🚧 Create Playlist - Coming soon!");
    }
    
    private void handleBrowsePublicPlaylists() {
        System.out.println("🚧 Browse Public Playlists - Coming soon!");
    }
    
    private void handleMyProfile() {
        System.out.println("🚧 My Profile - Coming soon!");
    }
    
    private void handleFindUsers() {
        System.out.println("🚧 Find Users - Coming soon!");
    }
    
    private void handleFollowingFollowers() {
        System.out.println("🚧 Following & Followers - Coming soon!");
    }
    
    private void handleUserSettings() {
        System.out.println("🚧 User Settings - Coming soon!");
    }
    
    private void handleAudioSettings() {
        System.out.println("🚧 Audio Settings - Coming soon!");
    }
    
    private void cleanup() {
        if (playbackController != null) {
            playbackController.cleanup();
        }
        scanner.close();
    }
}
