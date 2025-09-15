
import jep.SharedInterpreter;
import jep.JepException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Console Music Player ===");
        System.out.println("Powered by spotdl and yt-dlp");
        System.out.println("🚀 Performance Optimized Version");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        try (SharedInterpreter interp = new SharedInterpreter()) {
            System.out.println("⚡ Initializing optimized Python environment...");

            // Initialize Python environment with performance optimizations
            interp.exec("import sys");
            interp.exec("import os");
            interp.exec("sys.path.append('python/music_backend')");
            interp.exec("from downloader import search_and_download, check_dependencies");
            interp.exec("from player import play_audio_file, find_latest_audio_file, get_audio_file_count");

            // Quick dependency check for early feedback
            interp.exec("deps_ok = check_dependencies()");
            Boolean depsOk = (Boolean) interp.getValue("deps_ok");
            if (!depsOk) {
                System.out.println("⚠️  Warning: Some dependencies may not be optimal. Performance may be affected.");
            }

            // Show current library status
            interp.exec("file_count = get_audio_file_count()");
            Long fileCountLong = (Long) interp.getValue("file_count");
            int fileCount = fileCountLong.intValue();
            if (fileCount > 0) {
                System.out.println("📁 Found " + fileCount + " existing audio files in library");
            }

            System.out.println("✅ Ready! Enter song names to search and download.");

            while (true) {
                System.out.print("\n🎵 Enter song name (or 'quit' to exit): ");
                String songName = scanner.nextLine().trim();

                if (songName.equalsIgnoreCase("quit") || songName.equalsIgnoreCase("exit")) {
                    System.out.println("👋 Goodbye!");
                    break;
                }

                if (songName.isEmpty()) {
                    System.out.println("❌ Please enter a valid song name.");
                    continue;
                }

                // Show progress indicator
                long startTime = System.currentTimeMillis();
                System.out.println("🔍 Searching and downloading: " + songName);

                // Search and download the song using Python (now optimized)
                interp.set("song_name", songName);
                interp.exec("download_success = search_and_download(song_name)");

                // Check if download was successful
                Boolean downloadSuccess = (Boolean) interp.getValue("download_success");

                if (downloadSuccess) {
                    long downloadTime = System.currentTimeMillis() - startTime;
                    System.out.println("✅ Download completed in " + (downloadTime / 1000.0) + " seconds! Now playing...");

                    // Find and play the latest downloaded file (now optimized)
                    interp.exec("latest_file = find_latest_audio_file()");
                    String latestFile = (String) interp.getValue("latest_file");

                    if (latestFile != null) {
                        System.out.println("♪ Playing: " + new File(latestFile).getName());

                        long playStartTime = System.currentTimeMillis();
                        interp.set("file_to_play", latestFile);
                        interp.exec("play_success = play_audio_file(file_to_play)");

                        Boolean playSuccess = (Boolean) interp.getValue("play_success");
                        long playTime = System.currentTimeMillis() - playStartTime;

                        if (playSuccess) {
                            System.out.println("🎶 Playback started in " + playTime + "ms");
                        } else {
                            System.out.println("❌ Could not play the audio file. Please check your audio player.");
                        }
                    } else {
                        System.out.println("❌ No audio files found to play.");
                    }
                } else {
                    System.out.println("❌ Failed to download the song. Please try a different search term.");
                }

                // Show total operation time
                long totalTime = System.currentTimeMillis() - startTime;
                System.out.println("⏱️  Total operation time: " + (totalTime / 1000.0) + " seconds");
            }

        } catch (JepException e) {
            System.err.println("Error with Python integration: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
