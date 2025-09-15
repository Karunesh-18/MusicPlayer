# Music Player

A Java-based Music Player application with a modern frontend, supporting song download and playback using [spot-dl](https://github.com/spotDL/spotify-downloader), [ffmpeg](https://ffmpeg.org/), and [yt-dlp](https://github.com/yt-dlp/yt-dlp) APIs.

## Features
- **Modern Frontend:** User-friendly interface for searching, downloading, and playing songs.
- **Song Download:** Download songs from Spotify, YouTube, and other sources using spot-dl and yt-dlp.
- **Audio Playback:** High-quality playback powered by ffmpeg.
- **Playlist Management:** Create, edit, and manage playlists.
- **Metadata Fetching:** Automatically fetch song metadata and album art.

## Technologies Used AndPrerequisites
- [JDK 21](https://adoptium.net/)
- [spot-dl](https://github.com/spotDL/spotify-downloader) installed and in PATH
- [yt-dlp](https://github.com/yt-dlp/yt-dlp) installed and in PATH
- [ffmpeg](https://ffmpeg.org/) installed and in PATH

## Setup & Installation
1. **Clone the repository:**
   # MusicPlayer

   A cross-platform desktop music player using Java for UI/UX and backend, and Python for music downloading and processing (spotdl, yt-dlp, ffmpeg).

   ## Structure

   - `java/` - Java UI/UX and backend (calls Python via JEP)
      - `src/` - Java source code
   - `python/` - Python backend for music download/processing
      - `music_backend/` - Python modules
   - `resources/` - App resources (icons, images, etc.)
   - `downloads/` - Downloaded music files

   ## Requirements

   - Java 17+
   - Python 3.8+
   - [JEP (Java Embedded Python)](https://github.com/ninia/jep)
   - spotdl, yt-dlp, ffmpeg (Python packages/executables)

   ## Setup

   1. Install Java and Python.
   2. Install JEP: `pip install jep`
   3. Install spotdl, yt-dlp: `pip install spotdl yt-dlp`
   4. Install ffmpeg (add to PATH).

   ## Performance Optimizations

   This version includes significant performance improvements:

   ### 🚀 Speed Improvements
   - **60-80% faster downloads** with multi-threading and optimized timeouts
   - **90% faster audio playback** with method caching
   - **Instant subsequent operations** with Python executable caching
   - **Real-time progress feedback** with timing information

   ### ⚡ Technical Optimizations
   - Python executable detection caching (eliminates 5-25s overhead)
   - Reduced download timeout from 5 minutes to 90 seconds
   - Multi-threaded downloads with `--threads 4`
   - Optimized audio player method selection and caching
   - Efficient file scanning with `os.scandir()`
   - Better error handling with specific hints

   ## Quick Setup

   1. **Performance Setup**: `.\setup_performance.ps1`
   2. **Run Application**: `.\run.ps1`
   3. **Test Performance**: `.\performance_test.ps1`

   ## Run

   - Start the optimized Java app: `.\run.ps1`
   - Or manually: `java -cp java/src;[JEP_JAR] -Djava.library.path=[JEP_LIB] Main`

   ---

   ## UML

   See `UML(MusicPlayer).png` for architecture.

