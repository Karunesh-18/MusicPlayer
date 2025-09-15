# music_backend/player.py

import subprocess
import platform
import os
import sys
import threading

# Global cache for the best working audio player method (performance optimization)
_best_player_method = None
_player_cache_lock = threading.Lock()

def _try_windows_player_method(file_path, method_name):
    """
    Try a specific Windows audio player method.
    Returns (success, method_name) tuple.
    """
    try:
        if method_name == 'os.startfile':
            os.startfile(file_path)
            return True, method_name
        elif method_name == 'start_command':
            result = subprocess.run(['start', '', file_path], shell=True,
                                  capture_output=True, text=True, timeout=2)  # Reduced timeout
            return result.returncode == 0, method_name
        elif method_name == 'powershell':
            subprocess.run(['powershell', '-c', f'Invoke-Item "{file_path}"'],
                         check=True, timeout=2)  # Reduced timeout
            return True, method_name
    except subprocess.TimeoutExpired:
        # Timeout often means the command worked but is running in background
        return True, method_name
    except Exception:
        return False, method_name

    return False, method_name

def play_audio_file(file_path):
    """
    Play an audio file using the system's default audio player.
    Optimized with method caching for faster subsequent plays.
    """
    global _best_player_method

    if not os.path.exists(file_path):
        print(f"Error: File not found: {file_path}")
        return False

    system = platform.system().lower()
    print(f"♪ Playing: {os.path.basename(file_path)}")

    try:
        if system == 'windows':
            # If we have a cached working method, try it first
            if _best_player_method:
                success, method = _try_windows_player_method(file_path, _best_player_method)
                if success:
                    return True
                else:
                    # Cached method failed, clear cache and try all methods
                    _best_player_method = None

            # Try methods in order of reliability and speed
            methods = ['os.startfile', 'start_command', 'powershell']

            for method in methods:
                success, method_name = _try_windows_player_method(file_path, method)
                if success:
                    # Cache the working method for next time
                    with _player_cache_lock:
                        _best_player_method = method_name
                    return True

            print('Could not play audio. Please check your audio player setup.')
            return False
                    
        elif system == 'darwin':  # macOS
            subprocess.run(['afplay', file_path], check=True)
            return True
            
        elif system == 'linux':
            # Try different Linux audio players
            players = ['mpg123', 'mpv', 'vlc', 'mplayer', 'paplay']
            for player in players:
                try:
                    subprocess.run([player, file_path], check=True)
                    return True
                except FileNotFoundError:
                    continue
            print('No suitable audio player found. Please install mpg123, mpv, vlc, or mplayer.')
            return False
        else:
            print(f'Unsupported operating system: {system}')
            return False
            
    except Exception as e:
        print(f'Error playing audio: {e}')
        return False

def find_latest_audio_file(directory="./downloads"):
    """
    Find the most recently downloaded audio file.
    Optimized for better performance with larger directories.
    """
    if not os.path.exists(directory):
        return None

    try:
        # Use os.scandir for better performance than os.listdir
        audio_extensions = {'.mp3', '.wav', '.flac', '.m4a', '.ogg'}  # Set for O(1) lookup
        latest_file = None
        latest_time = 0

        with os.scandir(directory) as entries:
            for entry in entries:
                if entry.is_file():
                    # Check extension efficiently
                    _, ext = os.path.splitext(entry.name.lower())
                    if ext in audio_extensions:
                        # Get modification time directly from DirEntry
                        mtime = entry.stat().st_mtime
                        if mtime > latest_time:
                            latest_time = mtime
                            latest_file = entry.path

        return latest_file

    except Exception as e:
        print(f"Error finding audio files: {e}")
        return None

def get_audio_file_count(directory="./downloads"):
    """
    Get count of audio files in directory (for progress feedback).
    """
    if not os.path.exists(directory):
        return 0

    try:
        audio_extensions = {'.mp3', '.wav', '.flac', '.m4a', '.ogg'}
        count = 0

        with os.scandir(directory) as entries:
            for entry in entries:
                if entry.is_file():
                    _, ext = os.path.splitext(entry.name.lower())
                    if ext in audio_extensions:
                        count += 1

        return count

    except Exception:
        return 0

if __name__ == "__main__":
    # Test the player
    latest_file = find_latest_audio_file()
    if latest_file:
        print(f"Found audio file: {latest_file}")
        play_audio_file(latest_file)
    else:
        print("No audio files found in downloads directory.")
