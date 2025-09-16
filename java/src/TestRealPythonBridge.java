import com.musicplayer.util.PythonBridge;

/**
 * Test the real PythonBridge implementation
 */
public class TestRealPythonBridge {
    public static void main(String[] args) {
        System.out.println("🧪 TESTING REAL PYTHON BRIDGE");
        System.out.println("==============================");
        
        try {
            // Initialize PythonBridge
            System.out.println("🔧 Initializing PythonBridge...");
            PythonBridge bridge = new PythonBridge();
            
            if (!bridge.isInitialized()) {
                System.err.println("❌ PythonBridge failed to initialize");
                return;
            }
            
            System.out.println("✅ PythonBridge initialized successfully");
            
            // Test download functionality
            System.out.println("\n🔍 Testing download functionality...");
            String testSong = "Powerhouse";
            System.out.println("Downloading: " + testSong);
            
            boolean downloadResult = bridge.downloadSong(testSong);
            System.out.println("Download result: " + (downloadResult ? "✅ SUCCESS" : "❌ FAILED"));
            
            if (downloadResult) {
                // Test finding latest file
                System.out.println("\n📁 Testing find latest file...");
                String latestFile = bridge.findLatestAudioFile();
                System.out.println("Latest file: " + (latestFile != null ? latestFile : "None found"));
                
                if (latestFile != null) {
                    // Test playback
                    System.out.println("\n🎵 Testing playback...");
                    boolean playResult = bridge.playAudioFile(latestFile);
                    System.out.println("Playback result: " + (playResult ? "✅ SUCCESS" : "❌ FAILED"));
                    
                    if (playResult) {
                        System.out.println("🎶 Audio should be playing now!");
                    }
                } else {
                    System.err.println("❌ No audio file found to play");
                }
            } else {
                System.err.println("❌ Download failed, cannot test playback");
            }
            
            // Test audio file count
            System.out.println("\n📊 Testing audio file count...");
            int fileCount = bridge.getAudioFileCount();
            System.out.println("Audio files in library: " + fileCount);
            
            System.out.println("\n🎉 ALL TESTS COMPLETED!");
            System.out.println("Real PythonBridge is " + (downloadResult ? "WORKING" : "NOT WORKING"));
            
        } catch (Exception e) {
            System.err.println("❌ Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
