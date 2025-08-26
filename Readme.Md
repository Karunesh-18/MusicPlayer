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

