
# Music Player

A cross-platform desktop music player using Java for UI/UX and backend, and Python for music downloading and processing (spotdl, yt-dlp, ffmpeg).

## Features
- **Modern Frontend:** User-friendly interface for searching, downloading, and playing songs.
- **Song Download:** Download songs from Spotify, YouTube, and other sources using spot-dl and yt-dlp.
- **Audio Playback:** High-quality playback powered by ffmpeg.
- **Playlist Management:** Create, edit, and manage playlists.
- **Metadata Fetching:** Automatically fetch song metadata and album art.

## Structure
- `java/` - Java UI/UX and backend (calls Python via JEP)
  - `src/` - Java source code
- `python/` - Python backend for music download/processing
  - `music_backend/` - Python modules
- `resources/` - App resources (icons, images, etc.)
- `downloads/` - Downloaded music files

## Technologies Used And Prerequisites
- Java 17+ or [JDK 21](https://adoptium.net/)
- Python 3.8+
- [JEP (Java Embedded Python)](https://github.com/ninia/jep)
- [spot-dl](https://github.com/spotDL/spotify-downloader) installed and in PATH
- [yt-dlp](https://github.com/yt-dlp/yt-dlp) installed and in PATH
- [ffmpeg](https://ffmpeg.org/) installed and in PATH

## Setup & Installation
1. Clone the repository
2. Install Java and Python
3. Install JEP: `pip install jep`
4. Install spotdl, yt-dlp: `pip install spotdl yt-dlp`
5. Install ffmpeg (add to PATH)

## Run
- Start the Java app (which will call Python backend via JEP)

---

## UML

See `UML(MusicPlayer).png` for architecture.
   See `UML(MusicPlayer).png` for architecture.

# Music Player

A Java-based Music Player application with a modern frontend, supporting song download and playback using spot-dl, ffmpeg, and yt-dlp APIs.

## Features
- **Modern Frontend:** User-friendly interface for searching, downloading, and playing songs.
- **Song Download:** Download songs from Spotify, YouTube, and other sources using spot-dl and yt-dlp.
- **Audio Playback:** High-quality playback powered by ffmpeg.
- **Playlist Management:** Create, edit, and manage playlists.
- **Metadata Fetching:** Automatically fetch song metadata and album art.
- **Extra Functionalities:**
  - Social sharing of playlists and songs
  - User preferences and repeat modes
  - Advanced search and filtering
  - Integration with Python backend for music processing
  - Download progress and error handling

## Technologies Used And Prerequisites
- JDK 21
- spot-dl installed and in PATH
- yt-dlp installed and in PATH
- ffmpeg installed and in PATH

## Setup & Installation
1. Clone the repository

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
- JEP (Java Embedded Python)
- spotdl, yt-dlp, ffmpeg (Python packages/executables)

## Setup
1. Install Java and Python.
2. Install JEP: `pip install jep`
3. Install spotdl, yt-dlp: `pip install spotdl yt-dlp`
4. Install ffmpeg (add to PATH).

## Run
- Start the Java app (which will call Python backend via JEP).

## UML
See `UML(MusicPlayer).png` for architecture.

