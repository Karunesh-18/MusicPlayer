# Music Player

A cross-platform desktop music player using Java for UI/UX and backend, and Python for music downloading and processing (spotdl, yt-dlp, ffmpeg).

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
- Start the Java app (which will call Python backend via JEP).

---


## Object-Oriented Programming (OOPS) Concepts

This project makes extensive use of OOPS principles in its design:

- **Encapsulation:** Classes like `Song`, `Playlist`, `User`, and `AudioPlayer` encapsulate related data and methods, exposing only necessary functionality through public methods.
- **Inheritance:** Classes such as `LocalAudioPlayer` extend `AudioPlayer`, inheriting playback functionality and allowing for specialized behavior.
- **Polymorphism:** Interfaces and abstract classes (e.g., `Repository`, `SongRepository`, `UserRepository`) allow different implementations to be used interchangeably, such as different types of players or repositories.
- **Abstraction:** Abstract classes and interfaces define common operations (e.g., play, pause, download) without specifying exact implementation, letting subclasses provide concrete behavior.
- **Composition:** Complex objects like `Playlist` are composed of multiple `Song` objects, and `User` may have preferences and playlists as part of their state.

These concepts help organize code, promote reusability, and make the system easier to maintain and extend.

## UML
See `UML(MusicPlayer).png` for architecture.

## References

- [JEP (Java Embedded Python)](https://github.com/ninia/jep)
- [spot-dl](https://github.com/spotDL/spotify-downloader)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- [ffmpeg](https://ffmpeg.org/)


