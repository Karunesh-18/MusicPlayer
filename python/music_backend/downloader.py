# music_backend/downloader.py

import subprocess
import sys
import os
from dotenv import load_dotenv

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '..', '.env'))

def get_python_executable():
    """
    Get the correct Python executable, handling JEP environment.
    """
    # Check if we're running in JEP (Java Embedded Python)
    if 'java' in sys.executable.lower():
        # We're running in JEP, need to find the actual Python executable
        # Try common Python paths
        python_paths = [
            'python',
            'python3',
            r'C:\Users\karun\AppData\Local\Programs\Python\Python313\python.exe',
            r'C:\Python313\python.exe',
            r'C:\Python\python.exe'
        ]

        for python_path in python_paths:
            try:
                result = subprocess.run([python_path, '--version'],
                                      capture_output=True, text=True, timeout=5)
                if result.returncode == 0:
                    return python_path
            except:
                continue

        # If we can't find Python, try to use the one from PATH
        return 'python'
    else:
        # Normal Python execution
        return sys.executable

def download_song(query: str, output_dir: str = "./downloads"):
    """
    Download a song using spotdl by searching for it.
    """
    # Get the correct Python executable
    python_exe = get_python_executable()

    # Ensure output directory exists
    try:
        os.makedirs(output_dir, exist_ok=True)
    except Exception as e:
        print(f"ERROR: Failed to create output directory '{output_dir}': {e}")
        return False

    # Prepare the command
    command = [
        python_exe, '-m', 'spotdl', 'download', query,
        '--output', output_dir,
        '--format', 'mp3'
    ]

    try:
        # Use spotdl to search and download
        result = subprocess.run(
            command,
            capture_output=True,
            text=True,
            check=True,
            timeout=300  # 5 minute timeout
        )

        print(f"Successfully downloaded: {query}")
        return True

    except subprocess.TimeoutExpired:
        print(f"ERROR: Download timed out for '{query}' after 5 minutes")
        return False

    except subprocess.CalledProcessError as e:
        print(f"ERROR: Download failed for '{query}'")

        # Try to provide more specific error messages
        if e.stderr:
            if "No results found" in e.stderr:
                print("HINT: No results found. Try a different search term or include artist name.")
            elif "network" in e.stderr.lower() or "connection" in e.stderr.lower():
                print("HINT: Network connection issue. Check your internet connection.")
            elif "ffmpeg" in e.stderr.lower():
                print("HINT: FFmpeg issue. Make sure FFmpeg is installed and in PATH.")
            elif "youtube" in e.stderr.lower():
                print("HINT: YouTube access issue. This might be a temporary problem.")

        return False

    except Exception as e:
        print(f"ERROR: Unexpected error: {type(e).__name__}: {e}")
        return False

def search_and_download(song_name: str, output_dir: str = "./downloads"):
    """
    Search for a song by name and download it.
    """
    print(f"Searching for: {song_name}")

    # Check if the song name is valid
    if not song_name or song_name.strip() == "":
        print("ERROR: Empty or invalid song name provided")
        return False

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
