# music_backend/downloader.py


import subprocess
import sys
import os
from dotenv import load_dotenv

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '..', '.env'))

def download_song(query: str, output_dir: str = "./downloads"):
    """
    Download a song using spotdl by searching for it.
    """
    try:
        # Use spotdl to search and download
        result = subprocess.run([
            sys.executable, '-m', 'spotdl', query,
            '--output', output_dir,
            '--format', 'mp3'
        ], capture_output=True, text=True, check=True)

        print(f"Successfully downloaded: {query}")
        return True
    except subprocess.CalledProcessError as e:
        print(f"Download failed for '{query}': {e.stderr}")
        return False
    except Exception as e:
        print(f"Unexpected error: {e}")
        return False

def search_and_download(song_name: str, output_dir: str = "./downloads"):
    """
    Search for a song by name and download it.
    """
    print(f"Searching for: {song_name}")
    return download_song(song_name, output_dir)

if __name__ == "__main__":
    # Example usage
    test_song = "Never Gonna Give You Up Rick Astley"
    print(f"Testing download with song: {test_song}")
    success = search_and_download(test_song)
    if success:
        print("Download completed successfully!")
    else:
        print("Download failed!")
