# music_backend/downloader.py


import subprocess
import sys
import os
import logging
from dotenv import load_dotenv

# Set up logging
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '..', '.env'))

def download_song(query: str, output_dir: str = "./downloads"):
    """
    Download a song using spotdl by searching for it.
    """
    logger.info(f"Starting download for query: '{query}'")
    logger.info(f"Output directory: {output_dir}")

    # Ensure output directory exists
    try:
        os.makedirs(output_dir, exist_ok=True)
        logger.info(f"Output directory created/verified: {output_dir}")
    except Exception as e:
        logger.error(f"Failed to create output directory '{output_dir}': {e}")
        print(f"ERROR: Failed to create output directory '{output_dir}': {e}")
        return False

    # Check if spotdl is available
    try:
        check_result = subprocess.run([
            sys.executable, '-m', 'spotdl', '--version'
        ], capture_output=True, text=True, timeout=10)
        logger.info(f"spotdl version check - Return code: {check_result.returncode}")
        logger.info(f"spotdl version output: {check_result.stdout}")
        if check_result.stderr:
            logger.warning(f"spotdl version stderr: {check_result.stderr}")
    except subprocess.TimeoutExpired:
        logger.error("spotdl version check timed out")
        print("ERROR: spotdl version check timed out")
        return False
    except Exception as e:
        logger.error(f"Failed to check spotdl version: {e}")
        print(f"ERROR: Failed to check spotdl version: {e}")
        return False

    # Prepare the command
    command = [
        sys.executable, '-m', 'spotdl', 'download', query,
        '--output', output_dir,
        '--format', 'mp3',
        '--print-errors'  # Use print-errors flag for more detailed output
    ]

    logger.info(f"Executing command: {' '.join(command)}")
    print(f"DEBUG: Executing command: {' '.join(command)}")

    try:
        # Use spotdl to search and download
        result = subprocess.run(
            command,
            capture_output=True,
            text=True,
            check=True,
            timeout=300  # 5 minute timeout
        )

        logger.info(f"Command completed successfully")
        logger.info(f"stdout: {result.stdout}")
        if result.stderr:
            logger.warning(f"stderr: {result.stderr}")

        print(f"Successfully downloaded: {query}")
        print(f"DEBUG: stdout: {result.stdout}")
        if result.stderr:
            print(f"DEBUG: stderr: {result.stderr}")

        return True

    except subprocess.TimeoutExpired:
        logger.error(f"Download timed out for '{query}' after 5 minutes")
        print(f"ERROR: Download timed out for '{query}' after 5 minutes")
        return False

    except subprocess.CalledProcessError as e:
        logger.error(f"Download failed for '{query}' - Return code: {e.returncode}")
        logger.error(f"Command that failed: {' '.join(e.cmd)}")
        logger.error(f"stdout: {e.stdout}")
        logger.error(f"stderr: {e.stderr}")

        print(f"ERROR: Download failed for '{query}'")
        print(f"DEBUG: Return code: {e.returncode}")
        print(f"DEBUG: Command: {' '.join(e.cmd)}")
        print(f"DEBUG: stdout: {e.stdout}")
        print(f"DEBUG: stderr: {e.stderr}")

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
        logger.error(f"Unexpected error during download: {type(e).__name__}: {e}")
        print(f"ERROR: Unexpected error: {type(e).__name__}: {e}")
        return False

def search_and_download(song_name: str, output_dir: str = "./downloads"):
    """
    Search for a song by name and download it.
    """
    logger.info(f"search_and_download called with song_name: '{song_name}', output_dir: '{output_dir}'")
    print(f"Searching for: {song_name}")

    # Check if the song name is valid
    if not song_name or song_name.strip() == "":
        logger.error("Empty or invalid song name provided")
        print("ERROR: Empty or invalid song name provided")
        return False

    # Log system information
    logger.info(f"Python executable: {sys.executable}")
    logger.info(f"Current working directory: {os.getcwd()}")
    logger.info(f"PATH environment variable: {os.environ.get('PATH', 'Not set')}")

    result = download_song(song_name, output_dir)

    if result:
        # Check if files were actually downloaded
        try:
            if os.path.exists(output_dir):
                files = [f for f in os.listdir(output_dir) if f.endswith(('.mp3', '.wav', '.flac', '.m4a'))]
                logger.info(f"Audio files found in {output_dir}: {files}")
                print(f"DEBUG: Audio files in directory: {files}")
                if not files:
                    logger.warning("Download reported success but no audio files found")
                    print("WARNING: Download reported success but no audio files found")
            else:
                logger.warning(f"Output directory {output_dir} does not exist after download")
                print(f"WARNING: Output directory {output_dir} does not exist after download")
        except Exception as e:
            logger.error(f"Error checking downloaded files: {e}")
            print(f"ERROR: Error checking downloaded files: {e}")

    return result

def test_environment():
    """
    Test the environment setup for downloading.
    """
    print("=== Environment Test ===")
    logger.info("Starting environment test")

    # Test Python
    print(f"Python executable: {sys.executable}")
    print(f"Python version: {sys.version}")

    # Test current directory
    print(f"Current working directory: {os.getcwd()}")

    # Test spotdl availability
    try:
        result = subprocess.run([sys.executable, '-m', 'spotdl', '--version'],
                              capture_output=True, text=True, timeout=10)
        print(f"spotdl version check - Return code: {result.returncode}")
        print(f"spotdl output: {result.stdout}")
        if result.stderr:
            print(f"spotdl stderr: {result.stderr}")
    except Exception as e:
        print(f"ERROR: Failed to check spotdl: {e}")

    # Test yt-dlp availability
    try:
        result = subprocess.run([sys.executable, '-m', 'yt_dlp', '--version'],
                              capture_output=True, text=True, timeout=10)
        print(f"yt-dlp version check - Return code: {result.returncode}")
        print(f"yt-dlp output: {result.stdout}")
        if result.stderr:
            print(f"yt-dlp stderr: {result.stderr}")
    except Exception as e:
        print(f"ERROR: Failed to check yt-dlp: {e}")

    # Test ffmpeg availability
    try:
        result = subprocess.run(['ffmpeg', '-version'],
                              capture_output=True, text=True, timeout=10)
        print(f"ffmpeg version check - Return code: {result.returncode}")
        print(f"ffmpeg output (first 200 chars): {result.stdout[:200]}")
    except Exception as e:
        print(f"ERROR: Failed to check ffmpeg: {e}")

    print("=== Environment Test Complete ===")

if __name__ == "__main__":
    # Run environment test first
    test_environment()
    print()

    # Example usage
    test_song = "Never Gonna Give You Up Rick Astley"
    print(f"Testing download with song: {test_song}")
    success = search_and_download(test_song)
    if success:
        print("Download completed successfully!")
    else:
        print("Download failed!")
