import java.io.*;

/**
 * Test script for Spotify Console with Tamil songs
 */
public class TestSpotifyConsole {
    public static void main(String[] args) {
        System.out.println("🎵 TESTING SPOTIFY CONSOLE WITH TAMIL SONGS");
        System.out.println("===========================================");
        
        try {
            // Create input simulation for testing
            String input = "3\n7\n1\n9\n"; // Guest login -> Quick Play Tamil -> Vaathi Coming -> Exit
            
            // Set up input stream
            InputStream originalIn = System.in;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            // Run the Spotify Console
            SpotifyConsole.main(new String[]{});
            
            // Restore original input
            System.setIn(originalIn);
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
