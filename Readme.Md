# Music Player

A hybrid Java-Python console-based music player that demonstrates cross-language integration. The application uses Java for the main interface and application logic, while leveraging Python for music downloading and audio playback functionality.

## Features
- **Console Interface:** Interactive menu-driven interface for music operations
- **Music Download:** Download songs from various sources using spotdl and yt-dlp
- **Audio Playback:** System-integrated audio playback using default media players
- **Music Library Management:** Automatic library building from downloaded files
- **Queue Management:** Add, remove, and navigate through song queues
- **Smart Search:** Search existing library or download new songs on-demand
- **Play Statistics:** Track play counts and song metadata
- **Cross-Platform:** Works on Windows with optimized audio player detection

## Project Structure
```
MusicPlayer/
├── java/src/
│   ├── SimpleMusicPlayer.java          # Main application entry point
│   └── com/musicplayer/
│       ├── model/Song.java             # Song data model
│       └── util/PythonBridge.java      # Java-Python integration bridge
├── python/
│   ├── music_backend/
│   │   ├── downloader.py               # Music downloading functionality
│   │   ├── player.py                   # Audio playback management
│   │   ├── main.py                     # Python backend entry point
│   │   └── __init__.py
│   └── requirements.txt                # Python dependencies
└── downloads/                          # Downloaded music files (auto-created)
```

## Technologies Used
- **Java 8+** - Main application framework
- **Python 3.8+** - Backend processing
- **spotdl** - Music downloading from streaming platforms
- **yt-dlp** - YouTube audio extraction
- **ProcessBuilder** - Java-Python inter-process communication

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Python 3.8 or higher
- Internet connection for downloading music

## Setup & Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd MusicPlayer
```

### 2. Install Python Dependencies
```bash
cd python
pip install -r requirements.txt
```

### 3. Compile Java Application
```bash
cd java/src
javac -d . SimpleMusicPlayer.java com/musicplayer/model/Song.java com/musicplayer/util/PythonBridge.java
```

### 4. Run the Application
```bash
java SimpleMusicPlayer
```

---


## How to Use

1. **Start the Application:** Run the compiled Java application
2. **Main Menu Options:**
   - **Play a song:** Search your library or download new songs
   - **Manage queue:** Add/remove songs, navigate playback
   - **Browse library:** View all downloaded songs
   - **Quit:** Exit the application

3. **Playing Music:**
   - Enter song name to search existing library
   - If not found, the app will attempt to download it
   - Songs are automatically added to your library

4. **Queue Management:**
   - Add songs to queue for continuous playback
   - Remove songs from queue
   - Navigate with next/previous controls
   - View current queue contents

## Architecture & Design

### Object-Oriented Programming (OOP) Concepts

This project demonstrates key OOP principles:

- **Encapsulation:** The `Song` class encapsulates all song-related data (title, artist, duration, file path, etc.) and provides controlled access through getter/setter methods
- **Data Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


a Hiding:** Private fields in classes like `Song` and `PythonBridge` protect internal state from direct external access
- **Constructor Overloading:** The `Song` class provides multiple constructors for different initialization scenarios
- **Method Overriding:** Standard `equals()`, `hashCode()`, and `toString()` methods are overridden in the `Song` class
- **Abstraction:** The `PythonBridge` class abstracts the complexity of Java-Python communication behind simple method calls
- **Composition:** The main application uses composition with `ArrayList<Song>` for library and queue management

### Data Structures Used

- **ArrayList<Song>:** For music library and play queue management
- **HashMap<String, Object>:** For caching download results in PythonBridge
- **ReentrantLock:** For thread-safe operations in PythonBridge
- **LocalDateTime:** For timestamp management
- **Primitive Types:** int, boolean, long for counters and flags

### Design Patterns

- **Bridge Pattern:** PythonBridge acts as a bridge between Java and Python components
- **Singleton-like Pattern:** Static instances in SimpleMusicPlayer for global state
- **Cache Pattern:** Result caching in PythonBridge for performance optimization

### Data Flow

1. **Application Startup:**
   - Java main method initializes PythonBridge
   - Scans downloads directory for existing music files
   - Builds initial music library from found files

2. **User Interaction:**
   - Console menu captures user input
   - Input validation and processing in Java

3. **Music Operations:**
   - **Search:** Java searches local library first
   - **Download:** If not found, Java calls Python via ProcessBuilder
   - **Playback:** Java delegates to Python player module
   - **Queue Management:** Java manages queue state internally

4. **Cross-Language Communication:**
   - Java → Python: ProcessBuilder executes Python scripts
   - Python → Java: Return codes and file system changes
   - Data persistence through file system (downloads directory)

## Performance Features

- **Caching:** Download results cached to avoid redundant operations
- **Thread Safety:** ReentrantLock ensures safe concurrent access
- **Lazy Loading:** Music library built on-demand from file system
- **Optimized Playback:** Best audio player method cached for subsequent use

## Error Handling

- **Input Validation:** User input sanitized and validated
- **Process Management:** Timeout mechanisms for Python process calls
- **Resource Cleanup:** Proper cleanup of processes and resources
- **Graceful Degradation:** Fallback mechanisms for failed operations

## Dependencies

### Java Dependencies
- Standard Java Library (no external JARs required)
- Uses built-in ProcessBuilder for inter-process communication

### Python Dependencies (requirements.txt)
- **spotdl (>=4.2.0):** Music downloading from streaming platforms
- **yt-dlp (>=2023.7.6):** YouTube audio extraction
- **python-dotenv (>=1.0.0):** Environment variable management
- **requests (>=2.31.0):** HTTP requests
- **urllib3 (>=2.0.4):** URL handling utilities

## UML Class Diagram

![UML Class Diagram](UML(MusicPlayer).png)

*Figure 1: UML Class Diagram showing the structure and relationships of the Music Player application components*

## References

- [spotdl - Spotify Downloader](https://github.com/spotDL/spotify-downloader)
- [yt-dlp - YouTube Downloader](https://github.com/yt-dlp/yt-dlp)
- [JEP - Java Embeded Python](https://github.com/ninia/jep)
- [Java ProcessBuilder Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html)


