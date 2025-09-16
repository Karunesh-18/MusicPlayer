import com.musicplayer.model.*;
import com.musicplayer.service.*;
import com.musicplayer.util.PythonBridge;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Console-based Spotify-like Music Player
 * Features: Download, Queue, Playlists, Search, User Management
 */
public class SpotifyConsole {
    private static Scanner scanner = new Scanner(System.in);
    private static PythonBridge pythonBridge;
    private static MusicLibraryService musicLibrary;
    private static UserService userService;
    private static PlaylistService playlistService;
    private static User currentUser;
    private static Queue<Song> playQueue = new LinkedList<>();
    private static Song currentlyPlaying = null;
    private static List<String> sessionDownloads = new ArrayList<>();
    private static boolean isPlaying = false;

    public static void main(String[] args) {
        System.out.println("🎵 SPOTIFY CONSOLE MUSIC PLAYER");
        System.out.println("===============================");
        
        try {
            initializeSystem();
            loginOrRegister();
            showMainMenu();
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private static void initializeSystem() {
        System.out.println("🔧 Initializing Spotify Console...");
        pythonBridge = new PythonBridge();
        musicLibrary = new MusicLibraryService();
        userService = new UserService();
        playlistService = new PlaylistService();
        loadExistingMusic();
        System.out.println("✅ System initialized!");
    }

    private static void loadExistingMusic() {
        File downloadsDir = new File("downloads");
        if (downloadsDir.exists()) {
            File[] files = downloadsDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".mp3"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String[] parts = fileName.replace(".mp3", "").split(" - ", 2);
                    String artist = parts.length > 1 ? parts[0] : "Unknown Artist";
                    String title = parts.length > 1 ? parts[1] : fileName.replace(".mp3", "");
                    
                    Song song = new Song(title, artist);
                    song.setFilePath(file.getAbsolutePath());
                    musicLibrary.addSong(song);
                }
                System.out.println("📚 Loaded " + files.length + " existing songs");
            }
        }
    }

    private static void loginOrRegister() {
        System.out.println("\n👤 USER LOGIN/REGISTRATION");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Continue as Guest");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 3 -> {
                currentUser = userService.registerUser("guest", "guest@example.com", "Guest User", "guest123");
                System.out.println("✅ Continuing as Guest");
            }
            default -> {
                System.out.println("Invalid choice, continuing as Guest");
                currentUser = userService.registerUser("guest", "guest@example.com", "Guest User", "guest123");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Optional<User> userOpt = userService.authenticateUser(username, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("✅ Welcome back, " + currentUser.getDisplayName() + "!");
        } else {
            System.out.println("❌ Invalid credentials. Continuing as Guest.");
            currentUser = userService.registerUser("guest", "guest@example.com", "Guest User", "guest123");
        }
    }

    private static void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Display Name: ");
        String displayName = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            currentUser = userService.registerUser(username, email, displayName, password);
            System.out.println("✅ Registration successful! Welcome, " + displayName + "!");
        } catch (Exception e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            System.out.println("Continuing as Guest.");
            currentUser = userService.registerUser("guest", "guest@example.com", "Guest User", "guest123");
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n🎵 SPOTIFY CONSOLE - MAIN MENU");
            System.out.println("==============================");
            if (currentlyPlaying != null) {
                System.out.println("🎶 Now Playing: " + currentlyPlaying.getDisplayName());
            }
            if (!playQueue.isEmpty()) {
                System.out.println("📋 Queue: " + playQueue.size() + " songs");
            }
            
            System.out.println("\n1. 🔍 Search & Download Music");
            System.out.println("2. 📚 Browse Library");
            System.out.println("3. ▶️ Playback Controls");
            System.out.println("4. 📋 Queue Management");
            System.out.println("5. 🎵 Playlist Management");
            System.out.println("6. 👤 User Profile");
            System.out.println("7. 🎯 Quick Play (Tamil Songs)");
            System.out.println("8. 📊 Statistics");
            System.out.println("9. ❌ Exit");
            System.out.print("\nChoose option: ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1 -> searchAndDownload();
                case 2 -> browseLibrary();
                case 3 -> playbackControls();
                case 4 -> queueManagement();
                case 5 -> playlistManagement();
                case 6 -> userProfile();
                case 7 -> quickPlayTamil();
                case 8 -> showStatistics();
                case 9 -> {
                    System.out.println("👋 Thanks for using Spotify Console!");
                    return;
                }
                default -> System.out.println("❌ Invalid option!");
            }
        }
    }

    private static void searchAndDownload() {
        System.out.println("\n🔍 SEARCH & DOWNLOAD");
        System.out.println("====================");
        System.out.print("Enter song name or artist: ");
        String query = scanner.nextLine();
        
        if (query.trim().isEmpty()) {
            System.out.println("❌ Please enter a search term");
            return;
        }
        
        System.out.println("⬇️ Downloading: " + query);
        System.out.println("⏳ Please wait (30-90 seconds)...");
        
        CompletableFuture.supplyAsync(() -> pythonBridge.downloadSong(query))
            .thenAccept(success -> {
                if (success) {
                    System.out.println("✅ Download completed!");
                    String latestFile = pythonBridge.findLatestAudioFile();
                    if (latestFile != null) {
                        addDownloadedSongToLibrary(latestFile, query);
                        sessionDownloads.add(latestFile);
                        
                        System.out.println("1. ▶️ Play Now");
                        System.out.println("2. ➕ Add to Queue");
                        System.out.println("3. 📋 Add to Playlist");
                        System.out.println("4. 🔙 Back to Menu");
                        System.out.print("Choose option: ");
                        
                        int choice = getIntInput();
                        Song newSong = findSongByPath(latestFile);
                        if (newSong != null) {
                            switch (choice) {
                                case 1 -> playNow(newSong);
                                case 2 -> addToQueue(newSong);
                                case 3 -> addSongToPlaylist(newSong);
                            }
                        }
                    }
                } else {
                    System.out.println("❌ Download failed!");
                }
            });
        
        // Wait for completion
        try {
            Thread.sleep(2000); // Give some time for the async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void addDownloadedSongToLibrary(String filePath, String originalQuery) {
        File file = new File(filePath);
        String fileName = file.getName();
        String[] parts = fileName.replace(".mp3", "").split(" - ", 2);
        String artist = parts.length > 1 ? parts[0] : "Unknown Artist";
        String title = parts.length > 1 ? parts[1] : originalQuery;
        
        Song song = new Song(title, artist);
        song.setFilePath(filePath);
        musicLibrary.addSong(song);
        
        System.out.println("📚 Added to library: " + song.getDisplayName());
    }

    private static Song findSongByPath(String filePath) {
        return musicLibrary.getAllSongs().stream()
            .filter(song -> filePath.equals(song.getFilePath()))
            .findFirst()
            .orElse(null);
    }

    private static void playNow(Song song) {
        currentlyPlaying = song;
        isPlaying = true;
        boolean success = pythonBridge.playAudioFile(song.getFilePath());
        if (success) {
            System.out.println("🎶 Now Playing: " + song.getDisplayName());
            song.play(); // Increment play count
            currentUser.addToRecentlyPlayed(song);
        } else {
            System.out.println("❌ Failed to play song");
            isPlaying = false;
            currentlyPlaying = null;
        }
    }

    private static void addToQueue(Song song) {
        playQueue.offer(song);
        System.out.println("➕ Added to queue: " + song.getDisplayName());
        System.out.println("📋 Queue now has " + playQueue.size() + " songs");
    }

    private static void addSongToPlaylist(Song song) {
        List<Playlist> userPlaylists = playlistService.getUserPlaylists(currentUser);
        if (userPlaylists.isEmpty()) {
            System.out.println("📋 No playlists found. Creating 'My Music' playlist...");
            Playlist defaultPlaylist = playlistService.createPlaylist("My Music", currentUser);
            defaultPlaylist.addSong(song);
            System.out.println("✅ Added to 'My Music' playlist");
        } else {
            System.out.println("📋 Select playlist:");
            for (int i = 0; i < userPlaylists.size(); i++) {
                System.out.println((i + 1) + ". " + userPlaylists.get(i).getName());
            }
            System.out.println((userPlaylists.size() + 1) + ". Create New Playlist");
            System.out.print("Choose: ");

            int choice = getIntInput();
            if (choice > 0 && choice <= userPlaylists.size()) {
                Playlist selectedPlaylist = userPlaylists.get(choice - 1);
                selectedPlaylist.addSong(song);
                System.out.println("✅ Added to '" + selectedPlaylist.getName() + "'");
            } else if (choice == userPlaylists.size() + 1) {
                System.out.print("Enter playlist name: ");
                String playlistName = scanner.nextLine();
                Playlist newPlaylist = playlistService.createPlaylist(playlistName, currentUser);
                newPlaylist.addSong(song);
                System.out.println("✅ Created playlist '" + playlistName + "' and added song");
            }
        }
    }

    private static void browseLibrary() {
        System.out.println("\n📚 MUSIC LIBRARY");
        System.out.println("================");

        List<Song> allSongs = musicLibrary.getAllSongs();
        if (allSongs.isEmpty()) {
            System.out.println("📭 Your library is empty. Download some music first!");
            return;
        }

        System.out.println("1. 🎵 All Songs (" + allSongs.size() + ")");
        System.out.println("2. 🎤 Browse by Artist");
        System.out.println("3. 🔍 Search Library");
        System.out.println("4. 🔥 Most Played");
        System.out.println("5. 🕒 Recently Added");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> showAllSongs();
            case 2 -> browseByArtist();
            case 3 -> searchLibrary();
            case 4 -> showMostPlayed();
            case 5 -> showRecentlyAdded();
        }
    }

    private static void showAllSongs() {
        List<Song> songs = musicLibrary.getAllSongs();
        System.out.println("\n🎵 ALL SONGS:");
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            System.out.println((i + 1) + ". " + song.getDisplayName() +
                             " (Played: " + song.getPlayCount() + " times)");
        }

        System.out.print("\nEnter song number to play (0 to go back): ");
        int choice = getIntInput();
        if (choice > 0 && choice <= songs.size()) {
            Song selectedSong = songs.get(choice - 1);
            System.out.println("1. ▶️ Play Now");
            System.out.println("2. ➕ Add to Queue");
            System.out.print("Choose: ");
            int action = getIntInput();
            if (action == 1) {
                playNow(selectedSong);
            } else if (action == 2) {
                addToQueue(selectedSong);
            }
        }
    }

    private static void browseByArtist() {
        List<Artist> artists = musicLibrary.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("📭 No artists found");
            return;
        }

        System.out.println("\n🎤 ARTISTS:");
        for (int i = 0; i < artists.size(); i++) {
            System.out.println((i + 1) + ". " + artists.get(i).getName());
        }

        System.out.print("Select artist (0 to go back): ");
        int choice = getIntInput();
        if (choice > 0 && choice <= artists.size()) {
            Artist selectedArtist = artists.get(choice - 1);
            List<Song> artistSongs = musicLibrary.getSongsByArtist(selectedArtist.getName());
            System.out.println("\n🎵 Songs by " + selectedArtist.getName() + ":");
            for (int i = 0; i < artistSongs.size(); i++) {
                System.out.println((i + 1) + ". " + artistSongs.get(i).getTitle());
            }
        }
    }

    private static void searchLibrary() {
        System.out.print("🔍 Search your library: ");
        String query = scanner.nextLine();
        List<Song> results = musicLibrary.searchSongs(query);

        if (results.isEmpty()) {
            System.out.println("📭 No songs found matching '" + query + "'");
        } else {
            System.out.println("\n🔍 Search Results:");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i).getDisplayName());
            }
        }
    }

    private static void showMostPlayed() {
        List<Song> mostPlayed = musicLibrary.getMostPlayedSongs(10);
        System.out.println("\n🔥 MOST PLAYED:");
        for (int i = 0; i < mostPlayed.size(); i++) {
            Song song = mostPlayed.get(i);
            System.out.println((i + 1) + ". " + song.getDisplayName() +
                             " (" + song.getPlayCount() + " plays)");
        }
    }

    private static void showRecentlyAdded() {
        List<Song> recent = musicLibrary.getRecentlyAddedSongs(10);
        System.out.println("\n🕒 RECENTLY ADDED:");
        for (int i = 0; i < recent.size(); i++) {
            System.out.println((i + 1) + ". " + recent.get(i).getDisplayName());
        }
    }

    private static void playbackControls() {
        System.out.println("\n▶️ PLAYBACK CONTROLS");
        System.out.println("====================");

        if (currentlyPlaying != null) {
            System.out.println("🎶 Currently Playing: " + currentlyPlaying.getDisplayName());
        } else {
            System.out.println("⏸️ Nothing is currently playing");
        }

        System.out.println("1. ⏸️ Pause/Resume");
        System.out.println("2. ⏭️ Next Song");
        System.out.println("3. ⏮️ Previous Song");
        System.out.println("4. 🔀 Shuffle Queue");
        System.out.println("5. 🔁 Repeat Mode");
        System.out.println("6. 🔊 Volume Control");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> togglePlayPause();
            case 2 -> playNext();
            case 3 -> playPrevious();
            case 4 -> shuffleQueue();
            case 5 -> toggleRepeat();
            case 6 -> volumeControl();
        }
    }

    private static void togglePlayPause() {
        if (currentlyPlaying != null) {
            isPlaying = !isPlaying;
            System.out.println(isPlaying ? "▶️ Resumed" : "⏸️ Paused");
        } else {
            System.out.println("❌ No song is currently playing");
        }
    }

    private static void playNext() {
        if (!playQueue.isEmpty()) {
            Song nextSong = playQueue.poll();
            playNow(nextSong);
        } else {
            System.out.println("📭 Queue is empty");
        }
    }

    private static void playPrevious() {
        System.out.println("⏮️ Previous song functionality not implemented yet");
    }

    private static void shuffleQueue() {
        if (!playQueue.isEmpty()) {
            List<Song> queueList = new ArrayList<>(playQueue);
            Collections.shuffle(queueList);
            playQueue.clear();
            playQueue.addAll(queueList);
            System.out.println("🔀 Queue shuffled!");
        } else {
            System.out.println("📭 Queue is empty");
        }
    }

    private static void toggleRepeat() {
        System.out.println("🔁 Repeat mode functionality not implemented yet");
    }

    private static void volumeControl() {
        System.out.println("🔊 Volume control functionality not implemented yet");
    }

    private static void queueManagement() {
        System.out.println("\n📋 QUEUE MANAGEMENT");
        System.out.println("===================");

        if (playQueue.isEmpty()) {
            System.out.println("📭 Queue is empty");
            return;
        }

        System.out.println("📋 Current Queue (" + playQueue.size() + " songs):");
        List<Song> queueList = new ArrayList<>(playQueue);
        for (int i = 0; i < queueList.size(); i++) {
            System.out.println((i + 1) + ". " + queueList.get(i).getDisplayName());
        }

        System.out.println("\n1. ▶️ Play Next in Queue");
        System.out.println("2. 🗑️ Clear Queue");
        System.out.println("3. ➕ Add Song to Queue");
        System.out.println("4. 🔀 Shuffle Queue");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> playNext();
            case 2 -> {
                playQueue.clear();
                System.out.println("🗑️ Queue cleared");
            }
            case 3 -> addSongToQueue();
            case 4 -> shuffleQueue();
        }
    }

    private static void addSongToQueue() {
        List<Song> allSongs = musicLibrary.getAllSongs();
        if (allSongs.isEmpty()) {
            System.out.println("📭 No songs in library");
            return;
        }

        System.out.println("🎵 Select song to add to queue:");
        for (int i = 0; i < Math.min(10, allSongs.size()); i++) {
            System.out.println((i + 1) + ". " + allSongs.get(i).getDisplayName());
        }

        System.out.print("Choose song (0 to cancel): ");
        int choice = getIntInput();
        if (choice > 0 && choice <= Math.min(10, allSongs.size())) {
            Song selectedSong = allSongs.get(choice - 1);
            addToQueue(selectedSong);
        }
    }

    private static void playlistManagement() {
        System.out.println("\n🎵 PLAYLIST MANAGEMENT");
        System.out.println("======================");

        System.out.println("1. 📋 View My Playlists");
        System.out.println("2. ➕ Create New Playlist");
        System.out.println("3. 🎵 Add Song to Playlist");
        System.out.println("4. ▶️ Play Playlist");
        System.out.print("Choose option: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> viewPlaylists();
            case 2 -> createPlaylist();
            case 3 -> addSongToPlaylistMenu();
            case 4 -> playPlaylist();
        }
    }

    private static void viewPlaylists() {
        List<Playlist> playlists = playlistService.getUserPlaylists(currentUser);
        if (playlists.isEmpty()) {
            System.out.println("📭 You don't have any playlists yet");
            return;
        }

        System.out.println("\n📋 YOUR PLAYLISTS:");
        for (int i = 0; i < playlists.size(); i++) {
            Playlist playlist = playlists.get(i);
            System.out.println((i + 1) + ". " + playlist.getName() +
                             " (" + playlist.getSongs().size() + " songs)");
        }
    }

    private static void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            Playlist playlist = playlistService.createPlaylist(name, currentUser);
            System.out.println("✅ Created playlist: " + playlist.getName());
        }
    }

    private static void addSongToPlaylistMenu() {
        List<Song> songs = musicLibrary.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("📭 No songs in library");
            return;
        }

        System.out.println("🎵 Select song:");
        for (int i = 0; i < Math.min(10, songs.size()); i++) {
            System.out.println((i + 1) + ". " + songs.get(i).getDisplayName());
        }

        System.out.print("Choose song: ");
        int choice = getIntInput();
        if (choice > 0 && choice <= Math.min(10, songs.size())) {
            Song selectedSong = songs.get(choice - 1);
            addSongToPlaylist(selectedSong);
        }
    }

    private static void playPlaylist() {
        List<Playlist> playlists = playlistService.getUserPlaylists(currentUser);
        if (playlists.isEmpty()) {
            System.out.println("📭 No playlists found");
            return;
        }

        System.out.println("📋 Select playlist to play:");
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println((i + 1) + ". " + playlists.get(i).getName());
        }

        System.out.print("Choose playlist: ");
        int choice = getIntInput();
        if (choice > 0 && choice <= playlists.size()) {
            Playlist selectedPlaylist = playlists.get(choice - 1);
            List<Song> playlistSongs = selectedPlaylist.getSongs();
            if (!playlistSongs.isEmpty()) {
                playQueue.clear();
                playQueue.addAll(playlistSongs);
                playNext();
                System.out.println("▶️ Playing playlist: " + selectedPlaylist.getName());
            } else {
                System.out.println("📭 Playlist is empty");
            }
        }
    }

    private static void userProfile() {
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

    private static void quickPlayTamil() {
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

        System.out.print("Choose song to download and play: ");
        int choice = getIntInput();

        String selectedSong;
        if (choice > 0 && choice <= tamilSongs.length) {
            selectedSong = tamilSongs[choice - 1];
        } else if (choice == tamilSongs.length + 1) {
            System.out.print("Enter Tamil song name: ");
            selectedSong = scanner.nextLine();
        } else {
            System.out.println("❌ Invalid choice");
            return;
        }

        System.out.println("⬇️ Downloading: " + selectedSong);
        System.out.println("⏳ Please wait...");

        boolean success = pythonBridge.downloadSong(selectedSong);
        if (success) {
            String latestFile = pythonBridge.findLatestAudioFile();
            if (latestFile != null) {
                addDownloadedSongToLibrary(latestFile, selectedSong);
                sessionDownloads.add(latestFile);
                Song newSong = findSongByPath(latestFile);
                if (newSong != null) {
                    playNow(newSong);
                    addToQueue(newSong); // Also add to queue for continuous play
                }
            }
        } else {
            System.out.println("❌ Download failed");
        }
    }

    private static void showStatistics() {
        System.out.println("\n📊 MUSIC STATISTICS");
        System.out.println("===================");
        System.out.println("👤 User: " + currentUser.getDisplayName());
        System.out.println("🎵 Total Songs: " + musicLibrary.getAllSongs().size());
        System.out.println("🎤 Total Artists: " + musicLibrary.getAllArtists().size());
        System.out.println("📋 Total Playlists: " + playlistService.getUserPlaylists(currentUser).size());
        System.out.println("📥 Session Downloads: " + sessionDownloads.size());
        System.out.println("🕒 Recently Played: " + currentUser.getRecentlyPlayed().size());
        System.out.println("📋 Current Queue: " + playQueue.size() + " songs");

        if (currentlyPlaying != null) {
            System.out.println("🎶 Now Playing: " + currentlyPlaying.getDisplayName());
        }

        // Show most played songs
        List<Song> mostPlayed = musicLibrary.getMostPlayedSongs(5);
        if (!mostPlayed.isEmpty()) {
            System.out.println("\n🔥 TOP 5 MOST PLAYED:");
            for (int i = 0; i < mostPlayed.size(); i++) {
                Song song = mostPlayed.get(i);
                System.out.println((i + 1) + ". " + song.getDisplayName() +
                                 " (" + song.getPlayCount() + " plays)");
            }
        }
    }

    private static void cleanup() {
        System.out.println("\n🧹 CLEANING UP SESSION...");

        // Delete songs that weren't downloaded in this session
        File downloadsDir = new File("downloads");
        if (downloadsDir.exists()) {
            File[] files = downloadsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (files != null) {
                int deletedCount = 0;
                for (File file : files) {
                    if (!sessionDownloads.contains(file.getAbsolutePath())) {
                        // This file existed before the session, keep it
                        continue;
                    }

                    // Check if song was actually played or added to playlist
                    Song song = findSongByPath(file.getAbsolutePath());
                    if (song != null && song.getPlayCount() == 0) {
                        // Song was downloaded but never played, delete it
                        if (file.delete()) {
                            deletedCount++;
                            System.out.println("🗑️ Deleted unused download: " + file.getName());
                        }
                    }
                }

                if (deletedCount > 0) {
                    System.out.println("✅ Cleaned up " + deletedCount + " unused downloads");
                } else {
                    System.out.println("✅ No cleanup needed - all downloads were used");
                }
            }
        }

        // Cleanup Python bridge
        if (pythonBridge != null) {
            pythonBridge.cleanup();
        }

        System.out.println("👋 Session ended. Thanks for using Spotify Console!");
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
