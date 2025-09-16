import com.musicplayer.model.*;
import com.musicplayer.service.*;
import com.musicplayer.util.PythonBridge;
import java.util.*;
import java.io.File;

/**
 * Interactive demo to search for and play "Monica"
 */
public class PlayMonica {
    public static void main(String[] args) {
        System.out.println("🎵 SPOTIFY-LIKE MUSIC PLAYER - MONICA SEARCH & PLAY");
        System.out.println("==================================================");
        
        try {
            // Initialize components
            System.out.println("🔧 Initializing music player...");
            PythonBridge pythonBridge = new PythonBridge();
            MusicLibraryService musicLibrary = new MusicLibraryService();
            UserService userService = new UserService();
            
            // Create user
            User user = userService.registerUser("musiclover", "user@example.com", "password123");
            System.out.println("✅ User logged in: " + user.getUsername());
            
            // Search for Monica in existing files
            System.out.println("\n🔍 Searching for 'Monica' in your music library...");
            
            File downloadsDir = new File("../../downloads");
            boolean foundMonica = false;
            
            if (downloadsDir.exists()) {
                File[] files = downloadsDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName().toLowerCase();
                        if (fileName.contains("monica") && fileName.endsWith(".mp3")) {
                            System.out.println("🎵 Found Monica song: " + file.getName());
                            
                            // Create song object
                            Song monicaSong = new Song("Monica", "Artist");
                            monicaSong.setFilePath(file.getAbsolutePath());
                            musicLibrary.addSong(monicaSong);
                            
                            // Play the song
                            System.out.println("▶️ Playing Monica...");
                            boolean playResult = pythonBridge.playAudioFile(file.getAbsolutePath());
                            
                            if (playResult) {
                                System.out.println("🎶 Now Playing: " + file.getName());
                                System.out.println("🎵 Enjoy your music!");
                                
                                // Add to user's recently played
                                user.addToRecentlyPlayed(monicaSong);
                                System.out.println("✅ Added to recently played");
                            } else {
                                System.out.println("❌ Failed to play the song");
                            }
                            
                            foundMonica = true;
                            break;
                        }
                    }
                }
            }
            
            if (!foundMonica) {
                System.out.println("🔍 No 'Monica' song found in local files.");
                System.out.println("⬇️ Attempting to download 'Monica'...");
                
                // Mock download Monica
                boolean downloadResult = pythonBridge.downloadSong("Monica");
                
                if (downloadResult) {
                    System.out.println("✅ Monica downloaded successfully!");
                    System.out.println("🎵 Creating song entry...");
                    
                    // Create mock Monica song
                    Song monicaSong = new Song("Monica", "Unknown Artist");
                    monicaSong.setGenre("Pop");
                    musicLibrary.addSong(monicaSong);
                    
                    System.out.println("🎶 Monica is ready to play!");
                    System.out.println("▶️ Mock playback started...");
                    
                    // Add to recently played
                    user.addToRecentlyPlayed(monicaSong);
                    System.out.println("✅ Added Monica to recently played");
                } else {
                    System.out.println("❌ Failed to download Monica");
                }
            }
            
            // Show user's music stats
            System.out.println("\n📊 YOUR MUSIC STATS:");
            System.out.println("Recently Played: " + user.getRecentlyPlayed().size() + " songs");
            System.out.println("Library Songs: " + musicLibrary.getAllSongs().size() + " songs");
            
            // Show recently played
            if (!user.getRecentlyPlayed().isEmpty()) {
                System.out.println("\n🎵 RECENTLY PLAYED:");
                for (Song song : user.getRecentlyPlayed()) {
                    System.out.println("  ♪ " + song.getDisplayName());
                }
            }
            
            System.out.println("\n🎉 Music session complete!");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
