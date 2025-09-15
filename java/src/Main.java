
import com.musicplayer.MusicPlayerApplication;

/**
 * Main entry point for the Spotify-like Music Player application.
 * Now uses the new OOP architecture with dependency injection.
 */
public class Main {
    public static void main(String[] args) {
        // Handle command line arguments
        boolean createDefaultUser = false;
        boolean runHealthCheck = false;

        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "--create-default-user" -> createDefaultUser = true;
                case "--health-check" -> runHealthCheck = true;
                case "--help" -> {
                    showHelp();
                    return;
                }
            }
        }

        // Create and initialize the application
        MusicPlayerApplication app = new MusicPlayerApplication();

        // Add shutdown hook for graceful cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Shutdown signal received...");
            app.shutdown();
        }));

        try {
            // Initialize the application
            if (!app.initialize()) {
                System.err.println("❌ Failed to initialize application");
                System.exit(1);
            }

            // Create default user if requested
            if (createDefaultUser) {
                app.createDefaultUser();
            }

            // Run health check if requested
            if (runHealthCheck) {
                if (!app.healthCheck()) {
                    System.err.println("❌ Health check failed");
                    System.exit(1);
                }
                System.out.println("✅ Health check passed - application is ready");
            }

            // Start the application
            app.start();

        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
            e.printStackTrace();
            app.shutdown();
            System.exit(1);
        }
    }

    private static void showHelp() {
        System.out.println("🎵 SPOTIFY-LIKE MUSIC PLAYER");
        System.out.println("============================");
        System.out.println();
        System.out.println("Usage: java Main [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --create-default-user    Create a default admin user (admin/admin123)");
        System.out.println("  --health-check          Run health check before starting");
        System.out.println("  --help                  Show this help message");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  🎵 Search and download music from YouTube");
        System.out.println("  �️  Advanced playback controls (shuffle, repeat, queue)");
        System.out.println("  📋 Create and manage playlists");
        System.out.println("  👤 User accounts and social features");
        System.out.println("  👥 Follow users and share playlists");
        System.out.println("  🔍 Browse music library by artist, genre, etc.");
        System.out.println("  ⚡ Performance optimized with caching");
        System.out.println();
        System.out.println("Requirements:");
        System.out.println("  - Java 17 or higher");
        System.out.println("  - Python 3.8+ with spotdl, yt-dlp, pygame");
        System.out.println("  - JEP (Java Embedded Python)");
        System.out.println();
    }
}
