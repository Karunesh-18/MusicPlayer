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
                print("DEBUG: Trying os.startfile to open with default program...")
                os.startfile(file_path)
                print("✓ Audio file opened with default program")
                print("  The song should now be playing in your default audio player.")
                return True
            except Exception as e:
                print(f"DEBUG: os.startfile failed: {e}")

            # Method 2: Try using start command without /wait
            try:
                print("DEBUG: Trying start command...")
                result = subprocess.run(['start', '', file_path], shell=True,
                                      capture_output=True, text=True, timeout=5)
                if result.returncode == 0:
                    print("✓ Audio file opened with start command")
                    return True
                else:
                    print(f"DEBUG: start command failed with return code: {result.returncode}")
                    if result.stderr:
                        print(f"DEBUG: stderr: {result.stderr}")
            except subprocess.TimeoutExpired:
                print("✓ Start command launched (timeout is normal)")
                return True
            except Exception as e:
                print(f"DEBUG: start command failed: {e}")

            # Method 3: Try VLC if installed
            try:
                print("DEBUG: Trying VLC...")
                subprocess.run(['vlc', '--intf', 'dummy', '--play-and-exit', file_path],
                             check=True, capture_output=True, timeout=3)
                print("✓ Playing with VLC")
                return True
            except FileNotFoundError:
                print("DEBUG: VLC not found")
            except subprocess.TimeoutExpired:
                print("✓ VLC launched (timeout is normal)")
                return True
            except Exception as e:
                print(f"DEBUG: VLC failed: {e}")

            # Method 4: Try Windows Media Player
            try:
                print("DEBUG: Trying Windows Media Player...")
                subprocess.run(['wmplayer', file_path], check=False, timeout=3)
                print("✓ Launched Windows Media Player")
                return True
            except FileNotFoundError:
                print("DEBUG: Windows Media Player not found")
            except subprocess.TimeoutExpired:
                print("✓ Windows Media Player launched (timeout is normal)")
                return True
            except Exception as e:
                print(f"DEBUG: Windows Media Player failed: {e}")

            # Method 5: Try PowerShell approach
            try:
                print("DEBUG: Trying PowerShell approach...")
                # Use a simpler PowerShell command that just opens the file
                subprocess.run(['powershell', '-c', f'Invoke-Item "{file_path}"'],
                             check=True, timeout=5)
                print("✓ Opened with PowerShell Invoke-Item")
                return True
            except subprocess.TimeoutExpired:
                print("✓ PowerShell command launched (timeout is normal)")
                return True
            except Exception as e:
                print(f"DEBUG: PowerShell approach failed: {e}")

            print('❌ Could not play audio with any method.')
            print('   Please ensure you have a default audio player set up in Windows.')
            print('   You can manually open the file from the downloads folder.')
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
