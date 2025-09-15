
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
                System.out.println("DEBUG: Setting up Python environment for download...");

                // Search and download the song using Python
                interp.set("song_name", songName);
                System.out.println("DEBUG: Song name set in Python: " + songName);

                try {
                    System.out.println("DEBUG: Calling search_and_download function...");
                    interp.exec("download_success = search_and_download(song_name)");
                    System.out.println("DEBUG: search_and_download function completed");
                } catch (JepException e) {
                    System.err.println("ERROR: Exception during download: " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }

                // Check if download was successful
                Boolean downloadSuccess = (Boolean) interp.getValue("download_success");
                System.out.println("DEBUG: Download success status: " + downloadSuccess);

                if (downloadSuccess) {
                    System.out.println("Download completed! Now playing...");

                    // Find and play the latest downloaded file
                    System.out.println("DEBUG: Looking for downloaded audio files...");
                    interp.exec("latest_file = find_latest_audio_file()");
                    String latestFile = (String) interp.getValue("latest_file");
                    System.out.println("DEBUG: Latest file found: " + latestFile);

                    if (latestFile != null) {
                        System.out.println("Playing: " + new File(latestFile).getName());
                        System.out.println("DEBUG: File path: " + latestFile);
                        System.out.println("DEBUG: File exists: " + new File(latestFile).exists());

                        interp.set("file_to_play", latestFile);
                        System.out.println("DEBUG: Attempting to play audio file...");
                        interp.exec("play_success = play_audio_file(file_to_play)");

                        Boolean playSuccess = (Boolean) interp.getValue("play_success");
                        System.out.println("DEBUG: Play success status: " + playSuccess);

                        if (!playSuccess) {
                            System.out.println("Could not play the audio file. Please check your audio player.");
                        } else {
                            System.out.println("Audio playback initiated successfully!");
                        }
                    } else {
                        System.out.println("No audio files found to play.");
                        System.out.println("DEBUG: Checking downloads directory...");

                        // Check what's in the downloads directory
                        File downloadsDir = new File("downloads");
                        if (downloadsDir.exists() && downloadsDir.isDirectory()) {
                            File[] files = downloadsDir.listFiles();
                            if (files != null && files.length > 0) {
                                System.out.println("DEBUG: Files in downloads directory:");
                                for (File file : files) {
                                    System.out.println("  - " + file.getName() + " (" + file.length() + " bytes)");
                                }
                            } else {
                                System.out.println("DEBUG: Downloads directory is empty");
                            }
                        } else {
                            System.out.println("DEBUG: Downloads directory does not exist");
                        }
                    }
                } else {
                    System.out.println("Failed to download the song. Please try a different search term.");
                    System.out.println("DEBUG: Check the console output above for detailed error information.");
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
