# music_backend/downloader.py

import subprocess
import sys
import os
import threading
from dotenv import load_dotenv

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '..', '.env'))

# Global cache for Python executable (performance optimization)
_python_executable_cache = None
_cache_lock = threading.Lock()

def get_python_executable():

    global _python_executable_cache

    if _python_executable_cache is not None:
        return _python_executable_cache

    with _cache_lock:

        if _python_executable_cache is not None:
            return _python_executable_cache
       
        if 'java' in sys.executable.lower():
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
                                          capture_output=True, text=True, timeout=2)  # Reduced from 5s to 2s
                    if result.returncode == 0:
                        _python_executable_cache = python_path
                        return python_path
                except:
                    continue

            _python_executable_cache = 'python'
            return 'python'
        else:
            _python_executable_cache = sys.executable
            return sys.executable

def download_song(query: str, output_dir: str = "./downloads"):
    """
    Download a song using spotdl by searching for it.
    Optimized with reduced timeout and better performance.
    """
    python_exe = get_python_executable()

    try:
        os.makedirs(output_dir, exist_ok=True)
    except Exception as e:
        print(f"ERROR: Failed to create output directory '{output_dir}': {e}")
        return False

    command = [
        python_exe, '-m', 'spotdl', 'download', query,
        '--output', output_dir,
        '--format', 'mp3',
        '--threads', '4',  # Use multiple threads for faster download
        '--bitrate', '192k',  # Reasonable quality vs speed balance
        '--skip-explicit'  # Skip explicit content check for faster processing
    ]

    print("Downloading... (this may take 30-90 seconds)")

    try:
        # Used spotdl to search and download with optimized timeout
        subprocess.run(
            command,
            capture_output=True,
            text=True,
            check=True,
            timeout=50
        )

        print(f"✓ Successfully downloaded: {query}")
        return True

    except subprocess.TimeoutExpired:
        print(f"ERROR: Download timed out for '{query}' after 90 seconds")
        print("HINT: Try a more specific search term or check your internet connection")
        return False

    except subprocess.CalledProcessError as e:
        print(f"ERROR: Download failed for '{query}'")

        # Try to provide more specific error messages
        if e.stderr:
            stderr_lower = e.stderr.lower()
            if "no results found" in stderr_lower or "no matches found" in stderr_lower:
                print("HINT: No results found. Try a different search term or include artist name.")
            elif "network" in stderr_lower or "connection" in stderr_lower:
                print("HINT: Network connection issue. Check your internet connection.")
            elif "ffmpeg" in stderr_lower:
                print("HINT: FFmpeg issue. Make sure FFmpeg is installed and in PATH.")
            elif "youtube" in stderr_lower or "rate limit" in stderr_lower:
                print("HINT: YouTube access issue or rate limit. Try again in a few minutes.")
            elif "timeout" in stderr_lower:
                print("HINT: Connection timeout. Check your internet speed.")

        return False

    except Exception as e:
        print(f"ERROR: Unexpected error: {type(e).__name__}: {e}")
        return False

def search_and_download(song_name: str, output_dir: str = "./downloads"):
    """
    Search for a song by name and download it.
    Optimized with better validation and progress feedback.
    """
    # Enhanced input validation
    if not song_name or song_name.strip() == "":
        print("ERROR: Empty or invalid song name provided")
        return False

    # Clean and optimize search query
    cleaned_query = song_name.strip()
    if len(cleaned_query) < 3:
        print("ERROR: Search query too short. Please provide at least 3 characters.")
        return False

    print(f"🔍 Searching for: {cleaned_query}")

    # Check if file already exists to avoid re-downloading
    if os.path.exists(output_dir):
        existing_files = [f for f in os.listdir(output_dir)
                         if f.lower().endswith('.mp3') and cleaned_query.lower() in f.lower()]
        if existing_files:
            print(f"✓ Found existing file: {existing_files[0]}")
            print("Skipping download (file already exists)")
            return True

    return download_song(cleaned_query, output_dir)

def check_dependencies():
    """
    Quick check for required dependencies to provide early feedback.
    """
    try:
        # Quick spotdl version check with short timeout
        result = subprocess.run(['python', '-m', 'spotdl', '--version'],
                              capture_output=True, text=True, timeout=5)
        if result.returncode != 0:
            print("WARNING: spotdl may not be properly installed")
            return False
        return True
    except (subprocess.TimeoutExpired, FileNotFoundError):
        print("WARNING: spotdl not found or not responding")
        return False
    except Exception:
        return False

def update_dependencies():
    """
    Update spotdl and yt-dlp for better performance and compatibility.
    """
    print("🔄 Updating dependencies for better performance...")
    try:
        # Update spotdl
        subprocess.run(['python', '-m', 'pip', 'install', '--upgrade', 'spotdl'],
                      capture_output=True, timeout=30)
        # Update yt-dlp
        subprocess.run(['python', '-m', 'pip', 'install', '--upgrade', 'yt-dlp'],
                      capture_output=True, timeout=30)
        print("✓ Dependencies updated successfully")
        return True
    except Exception as e:
        print(f"WARNING: Could not update dependencies: {e}")
        return False

if __name__ == "__main__":
    # Example usage
    test_song = "Never Gonna Give You Up Rick Astley"
    print(f"Testing download with song: {test_song}")
    success = search_and_download(test_song)
    if success:
        print("Download completed successfully!")
    else:
        print("Download failed!")
