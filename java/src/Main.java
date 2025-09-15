
import jep.SharedInterpreter;
import jep.JepException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Console Music Player ===");
        System.out.println("Powered by spotdl and yt-dlp");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        try (SharedInterpreter interp = new SharedInterpreter()) {
            // Initialize Python environment
            interp.exec("import sys");
            interp.exec("import os");
            interp.exec("sys.path.append('python/music_backend')");
            interp.exec("from downloader import search_and_download");
            interp.exec("from player import play_audio_file, find_latest_audio_file");

            while (true) {
                System.out.print("Enter song name (or 'quit' to exit): ");
                String songName = scanner.nextLine().trim();

                if (songName.equalsIgnoreCase("quit") || songName.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (songName.isEmpty()) {
                    System.out.println("Please enter a valid song name.");
                    continue;
                }

                System.out.println("Searching and downloading: " + songName);

                // Search and download the song using Python
                interp.set("song_name", songName);
                interp.exec("download_success = search_and_download(song_name)");

                // Check if download was successful
                Boolean downloadSuccess = (Boolean) interp.getValue("download_success");

                if (downloadSuccess) {
                    System.out.println("Download completed! Now playing...");

                    // Find and play the latest downloaded file
                    interp.exec("latest_file = find_latest_audio_file()");
                    String latestFile = (String) interp.getValue("latest_file");

                    if (latestFile != null) {
                        System.out.println("Playing: " + new File(latestFile).getName());
                        interp.set("file_to_play", latestFile);
                        interp.exec("play_success = play_audio_file(file_to_play)");

                        Boolean playSuccess = (Boolean) interp.getValue("play_success");
                        if (!playSuccess) {
                            System.out.println("Could not play the audio file. Please check your audio player.");
                        }
                    } else {
                        System.out.println("No audio files found to play.");
                    }
                } else {
                    System.out.println("Failed to download the song. Please try a different search term.");
                }

                System.out.println();
            }

        } catch (JepException e) {
            System.err.println("Error with Python integration: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
