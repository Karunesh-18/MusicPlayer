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
            # Try different Windows audio players
            try:
                # Use PowerShell to play audio
                subprocess.run([
                    'powershell', '-c', 
                    f'Add-Type -AssemblyName presentationCore; '
                    f'$mediaPlayer = New-Object system.windows.media.mediaplayer; '
                    f'$mediaPlayer.open([uri]"{file_path}"); '
                    f'$mediaPlayer.Play(); '
                    f'Start-Sleep -Seconds 5; '  # Play for 5 seconds as demo
                    f'$mediaPlayer.Stop()'
                ], check=True)
                return True
            except:
                try:
                    # Try using Windows Media Player
                    subprocess.run(['start', '', file_path], shell=True, check=True)
                    return True
                except:
                    print('Could not play audio. Please install a media player.')
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
