
# 🎵 Java Music Streaming Bot / Music Player

A modern, cross-platform music streaming and playback application built in **Java** with seamless **Python integration**. It supports playing, downloading, and managing songs from platforms like **YouTube** and **Spotify**, demonstrating strong **OOP** principles and modular design.

---

## 🚀 Features
- Play music using song names or direct links (YouTube/Spotify)
- Queue, skip, pause, and resume functionalities
- Playlist management for continuous streaming
- Download songs from Spotify, YouTube, and more using `spotdl` and `yt-dlp`
- High-quality audio playback powered by ffmpeg/JLayer
- Metadata fetching and album art
- Java-based GUI (JavaFX/Swing) and CLI support
- Cross-platform: Windows, Linux, macOS

---

## 🛠️ Tech Stack & Prerequisites

**Backend:**
- Java (core logic, bot framework, playback)
- Python integration (music extraction/streaming via `spotdl`, `yt-dlp`)
- JLayer / JavaZoom / ffmpeg (audio playback)

**Frontend:**
- JavaFX / Swing (GUI)
- CLI (terminal usage)

**Database (optional):**
- SQLite / MySQL (user preferences, playlists, history)

**Prerequisites:**
- [JDK 17+](https://adoptium.net/) (JDK 21 recommended)
- [spot-dl](https://github.com/spotDL/spotify-downloader) installed and in PATH
- [yt-dlp](https://github.com/yt-dlp/yt-dlp) installed and in PATH
- [ffmpeg](https://ffmpeg.org/) installed and in PATH

---

## 📦 Installation & Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Karunesh-18/MusicPlayer.git
   cd MusicPlayer
   ```
2. **Install dependencies:**
   - Install spot-dl, yt-dlp, and ffmpeg as per their documentation.
3. **Build the project:**
   - Use your preferred Java build tool (e.g., Maven/Gradle) to build the project.
4. **Run the application:**
   ```sh
   java -jar target/MusicPlayer.jar
   ```

---

## 💻 Usage
- **Search for a song:** Enter the song/artist name in the search bar.
- **Download:** Click the download button to fetch the song using spot-dl/yt-dlp.
- **Play:** Use the built-in player to listen to your downloaded songs.
- **Manage playlists:** Add or remove songs from playlists.

---

## 🎯 OOP Concepts Applied
1. **Encapsulation:** Playback, queue management, and user controls are enclosed within dedicated classes (e.g., `MusicPlayer` hides low-level logic).
2. **Abstraction:** Abstract classes/interfaces define contracts for audio sources (e.g., `AudioSource` for YouTube/Spotify).
3. **Inheritance:** Audio sources (YouTube, Spotify, Local Files) extend a base `AudioSource` class.
4. **Polymorphism:** A single method call (e.g., `play()`) can handle any audio source dynamically.
5. **Modularity:** Modules for playback, queue, integration, and UI.

---

## 🔌 Python Integration
- Python handles music downloading/streaming using `spotdl` and `yt-dlp`.
- Java communicates with Python via **ProcessBuilder** (direct script execution) or **gRPC/REST API** (for scalability).
- This hybrid model leverages Python's music APIs and Java’s OOP/UI strengths.

---

## 📁 Project Structure
- `/src` - Java source code
- `/resources` - Static resources (icons, images, etc.)
- `/downloads` - Downloaded songs

---

## 📜 License
[MIT](LICENSE)

## 🙏 Acknowledgements
- [spot-dl](https://github.com/spotDL/spotify-downloader)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- [ffmpeg](https://ffmpeg.org/)

---

## 🔌 Python Integration
- Python handles music downloading/streaming because of powerful libraries (`spotdl`, `yt-dlp`).
- Java communicates with Python using:
  - **ProcessBuilder** (execute Python scripts directly).  
  - **gRPC / REST API** (if needed for scalability).

This hybrid model leverages the **strength of Python for music APIs** and **Java’s strong OOP and UI support**.

---

## 📦 Installation & Setup
1. Install **Java (JDK 17+)**.  
2. Install **Python 3.9+** with `spotdl` / `yt-dlp`.  
3. Clone the repository:  
   ```bash
   git clone https://github.com/your-username/java-music-bot.git
   cd java-music-bot

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
   ```sh
   git clone https://github.com/Karunesh-18/MusicPlayer.git
   cd MusicPlayer
   ```
2. **Install dependencies:**
   - Install spot-dl, yt-dlp, and ffmpeg as per their documentation.
3. **Build the project:**

4. **Run the application:**
   ```sh
   java -jar target/MusicPlayer.jar
   ```

## Usage
- **Search for a song:** Enter the song/artist name in the search bar.
- **Download:** Click the download button to fetch the song using spot-dl/yt-dlp.
- **Play:** Use the built-in player to listen to your downloaded songs.
- **Manage playlists:** Add or remove songs from playlists.

## Project Structure
- `/src` - Java source code
- `/resources` - Static resources (icons, images, etc.)
- `/downloads` - Downloaded songs


## License
[MIT](LICENSE)

## Acknowledgements
- [spot-dl](https://github.com/spotDL/spotify-downloader)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- [ffmpeg](https://ffmpeg.org/)

