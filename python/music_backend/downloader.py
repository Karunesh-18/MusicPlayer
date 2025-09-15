# music_backend/downloader.py


import subprocess
import sys
import os
from dotenv import load_dotenv

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '..', '.env'))

def download_song(url: str, output_dir: str = "./downloads"):
    """
    Download a song using spotdl and yt-dlp.
    """
    try:
        # spotdl handles both Spotify and YouTube links
        subprocess.run([
            sys.executable, '-m', 'spotdl', url, '--output', output_dir
        ], check=True)
        print(f"Successfully downloaded song from: {url}")
    except Exception as e:
        print(f"Download failed: {e}")

if __name__ == "__main__":
    # Example usage - using a valid YouTube URL for testing
    test_url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"  # Rick Astley - Never Gonna Give You Up
    print(f"Testing download with URL: {test_url}")
    download_song(test_url)
