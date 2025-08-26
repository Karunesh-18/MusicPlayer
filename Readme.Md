# 🎵 Java Music Streaming Bot

A music streaming application built using **Java** with support for Python integration, designed to play songs from platforms like **YouTube** and **Spotify**.  
This project demonstrates the use of **Object-Oriented Programming (OOP)** principles to build a scalable and maintainable system.

---

## 🚀 Features
- Play music using names or direct links (YouTube/Spotify).
- Queue, skip, pause, and resume functionalities.
- Playlist support for continuous streaming.
- Backend integration with Python libraries (e.g., `spotdl`, `yt-dlp`) for music extraction.
- Java-based frontend for user interaction.
- Cross-platform compatibility (Windows, Linux, macOS).

---

## 🛠️ Tech Stack

### Backend
- **Java** → Core application logic and bot framework.
- **Python Integration** → Handles music extraction and streaming (via `spotdl` or `yt-dlp`).
- **JLayer / JavaZoom** → Audio playback in Java.

### Frontend
- **JavaFX / Swing** → GUI for user interaction and controls.
- **CLI Support** → Run in terminal for lightweight usage.

### Database (Optional)
- **SQLite / MySQL** 
  - Store user preferences
  - Save playlists and history
  - Manage configurations

---

## 🎯 OOP Concepts Applied

1. **Encapsulation**  
   - Audio playback, queue management, and user controls are enclosed within dedicated classes.  
   - Example: `MusicPlayer` class hides low-level playback logic and exposes only play/pause/stop methods.

2. **Abstraction**  
   - Abstract classes and interfaces define contracts for audio sources (e.g., `AudioSource` for YouTube/Spotify).  
   - The user doesn’t worry about underlying implementations.

3. **Inheritance**  
   - Different audio sources (YouTube, Spotify, Local Files) extend a base `AudioSource` class.  
   - Reduces redundancy and improves code reusability.

4. **Polymorphism**  
   - A single method call (e.g., `play()`) can handle any audio source dynamically.  
   - Makes the system flexible for future integrations.

5. **Modularity**  
   - The project is divided into modules:  
     - **Playback Module** → Handles streaming.  
     - **Queue Module** → Manages track order.  
     - **Integration Module** → Bridges Python and Java.  
     - **UI Module** → Provides frontend.  

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
