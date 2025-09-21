import com.musicplayer.model.Song;
import com.musicplayer.util.PythonBridge;
import java.io.File;
import java.util.*;

public class SimpleMusicPlayer {
    private static final Scanner scanner = new Scanner(System.in);
    private static PythonBridge pythonBridge;
    private static final List<Song> musicLibrary = new ArrayList<>();
    private static final List<Song> playQueue = new ArrayList<>();
    private static Song currentlyPlaying = null;
    private static int currentQueueIndex = -1;
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("MUSIC PLAYER");
        System.out.println("============");
        System.out.println();

        pythonBridge = new PythonBridge();
        if (!pythonBridge.isInitialized()) {
            System.err.println("Failed to initialize Python backend");
            return;
        }

        loadExistingMusic();
        runMainLoop();
        cleanup();
    }

    private static void loadExistingMusic() {
        File downloadsDir = new File("downloads");
        if (!downloadsDir.exists()) {
            return;
        }

        File[] files = downloadsDir.listFiles((dir, name) ->
            name.toLowerCase().endsWith(".mp3") ||
            name.toLowerCase().endsWith(".wav") ||
            name.toLowerCase().endsWith(".m4a"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String title = fileName.substring(0, fileName.lastIndexOf('.'));

                String artist = "Unknown Artist";
                if (title.contains(" - ")) {
                    String[] parts = title.split(" - ", 2);
                    artist = parts[0].trim();
                    title = parts[1].trim();
                }

                Song song = new Song(title, artist);
                song.setFilePath(file.getAbsolutePath());
                musicLibrary.add(song);
            }
        }
    }

    private static void runMainLoop() {
        while (running) {
            showMainMenu();
            int choice = getIntInput("Choose option: ");
            handleMenuChoice(choice);
        }
    }

    private static void showMainMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("=========================");
        System.out.println("1. Play song by name");
        System.out.println("2. Queue management");
        System.out.println("3. See songs in library (" + musicLibrary.size() + " songs)");
        System.out.println("0. Quit");
        System.out.println();
    }

    private static void handleMenuChoice(int choice) {
        if (choice == 1) {
            handlePlaySong();
        } else if (choice == 2) {
            handleQueueManagement();
        } else if (choice == 3) {
            handleBrowseLibrary();
        } else if (choice == 0) {
            running = false;
        } else {
            System.out.println("Invalid option. Try again.");
        }
    }

    private static void handlePlaySong() {
        System.out.println("\nPlay Song");
        System.out.println("=========");
        System.out.print("Enter song name: ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {
            System.out.println("Please enter a song name.");
            return;
        }

        // First check if song exists in library
        Song existingSong = findSongInLibrary(query);
        if (existingSong != null) {
            playSong(existingSong);
            return;
        }

        // Download and play new song
        System.out.println("Getting " + query + " for you...");

        boolean downloadSuccess = pythonBridge.downloadSong(query);
        if (downloadSuccess) {
            String latestFile = pythonBridge.findLatestAudioFile();
            if (latestFile != null) {
                File file = new File(latestFile);
                String fileName = file.getName();
                String title = fileName.substring(0, fileName.lastIndexOf('.'));

                String artist = "Unknown Artist";
                if (title.contains(" - ")) {
                    String[] parts = title.split(" - ", 2);
                    artist = parts[0].trim();
                    title = parts[1].trim();
                }

                Song song = new Song(title, artist);
                song.setFilePath(latestFile);
                musicLibrary.add(song);

                boolean playSuccess = pythonBridge.playAudioFile(latestFile);
                if (playSuccess) {
                    currentlyPlaying = song;
                    song.play();
                    System.out.println("Playing: " + song.getDisplayName());
                } else {
                    System.out.println("Downloaded but couldn't play the song");
                }
            } else {
                System.out.println("Download completed but couldn't find the file");
            }
        } else {
            System.out.println("Couldn't Find " + query);
        }
    }

    private static Song findSongInLibrary(String query) {
        String searchTerm = query.toLowerCase();
        for (Song song : musicLibrary) {
            if (song.getTitle().toLowerCase().contains(searchTerm) ||
                song.getArtist().toLowerCase().contains(searchTerm)) {
                return song;
            }
        }
        return null;
    }

    private static void playSong(Song song) {
        if (song.getFilePath() != null) {
            boolean success = pythonBridge.playAudioFile(song.getFilePath());
            if (success) {
                currentlyPlaying = song;
                song.play();
                System.out.println("Playing: " + song.getDisplayName());
            } else {
                System.out.println("Can't play that song right now");
            }
        } else {
            System.out.println("Song file not found");
        }
    }

    private static void handleQueueManagement() {
        System.out.println("\nQueue Management");
        System.out.println("================");
        System.out.println("1. Add song to queue");
        System.out.println("2. Remove song from queue");
        System.out.println("3. Play next song");
        System.out.println("4. Play previous song");
        System.out.println("5. Show queue");
        System.out.println("0. Go back");

        int choice = getIntInput("Choose option: ");

        if (choice == 1) {
            addToQueue();
        } else if (choice == 2) {
            removeFromQueue();
        } else if (choice == 3) {
            playNext();
        } else if (choice == 4) {
            playPrevious();
        } else if (choice == 5) {
            showQueue();
        }
    }

    private static void addToQueue() {
        System.out.print("Enter song name to add to queue: ");
        String query = scanner.nextLine().trim();

        Song song = findSongInLibrary(query);
        if (song != null) {
            playQueue.add(song);
            System.out.println("Added to queue: " + song.getDisplayName());
        } else {
            System.out.println("Song not found in library");
        }
    }

    private static void removeFromQueue() {
        if (playQueue.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        showQueue();
        int index = getIntInput("Enter song number to remove: ") - 1;

        if (index >= 0 && index < playQueue.size()) {
            Song removed = playQueue.remove(index);
            System.out.println("Removed from queue: " + removed.getDisplayName());
            if (currentQueueIndex > index) {
                currentQueueIndex--;
            }
        } else {
            System.out.println("Invalid song number");
        }
    }

    private static void playNext() {
        if (playQueue.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        currentQueueIndex++;
        if (currentQueueIndex >= playQueue.size()) {
            currentQueueIndex = 0;
        }

        Song nextSong = playQueue.get(currentQueueIndex);
        playSong(nextSong);
    }

    private static void playPrevious() {
        if (playQueue.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        currentQueueIndex--;
        if (currentQueueIndex < 0) {
            currentQueueIndex = playQueue.size() - 1;
        }

        Song prevSong = playQueue.get(currentQueueIndex);
        playSong(prevSong);
    }

    private static void showQueue() {
        if (playQueue.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        System.out.println("\nCurrent Queue:");
        for (int i = 0; i < playQueue.size(); i++) {
            Song song = playQueue.get(i);
            String marker = (i == currentQueueIndex) ? " -> " : "    ";
            System.out.println(marker + (i + 1) + ". " + song.getDisplayName());
        }
    }

    private static void handleBrowseLibrary() {
        System.out.println("\nMusic Library");
        System.out.println("=============");

        if (musicLibrary.isEmpty()) {
            System.out.println("No songs in library");
            return;
        }

        for (int i = 0; i < musicLibrary.size(); i++) {
            Song song = musicLibrary.get(i);
            System.out.println((i + 1) + ". " + song.getDisplayName() +
                             " (Played " + song.getPlayCount() + " times)");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void cleanup() {
        if (pythonBridge != null) {
            pythonBridge.cleanup();
        }
    }
}