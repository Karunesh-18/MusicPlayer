# music_backend/player.py

import subprocess
import platform
import os
import sys

def play_audio_file(file_path):
    """
    Play an audio file using the system's default audio player.
    """
    if not os.path.exists(file_path):
        print(f"Error: File not found: {file_path}")
        return False

    system = platform.system().lower()
    print(f"Playing: {os.path.basename(file_path)}")

    try:
        if system == 'windows':
            # Try different Windows audio players in order of preference

            # Method 1: Try os.startfile (simplest and most reliable)
            try:
                os.startfile(file_path)
                return True
            except Exception as e:
                pass

            # Method 2: Try using start command without /wait
            try:
                result = subprocess.run(['start', '', file_path], shell=True,
                                      capture_output=True, text=True, timeout=5)
                if result.returncode == 0:
                    return True
            except subprocess.TimeoutExpired:
                return True
            except Exception as e:
                pass

            # Method 3: Try VLC if installed
            try:
                subprocess.run(['vlc', '--intf', 'dummy', '--play-and-exit', file_path],
                             check=True, capture_output=True, timeout=3)
                return True
            except FileNotFoundError:
                pass
            except subprocess.TimeoutExpired:
                return True
            except Exception as e:
                pass

            # Method 4: Try Windows Media Player
            try:
                subprocess.run(['wmplayer', file_path], check=False, timeout=3)
                return True
            except FileNotFoundError:
                pass
            except subprocess.TimeoutExpired:
                return True
            except Exception as e:
                pass

            # Method 5: Try PowerShell approach
            try:
                subprocess.run(['powershell', '-c', f'Invoke-Item "{file_path}"'],
                             check=True, timeout=5)
                return True
            except subprocess.TimeoutExpired:
                return True
            except Exception as e:
                pass

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
    """
    if not os.path.exists(directory):
        return None
        
    audio_extensions = ['.mp3', '.wav', '.flac', '.m4a', '.ogg']
    audio_files = []
    
    for file in os.listdir(directory):
        if any(file.lower().endswith(ext) for ext in audio_extensions):
            file_path = os.path.join(directory, file)
            audio_files.append((file_path, os.path.getmtime(file_path)))
    
    if not audio_files:
        return None
    
    # Return the most recently modified file
    latest_file = max(audio_files, key=lambda x: x[1])
    return latest_file[0]

if __name__ == "__main__":
    # Test the player
    latest_file = find_latest_audio_file()
    if latest_file:
        print(f"Found audio file: {latest_file}")
        play_audio_file(latest_file)
    else:
        print("No audio files found in downloads directory.")
