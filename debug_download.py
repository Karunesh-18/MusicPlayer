#!/usr/bin/env python3
"""
Debug script for testing music download functionality.
This script provides detailed debugging information to help diagnose download issues.
"""

import sys
import os
import subprocess
import logging
from datetime import datetime

# Add the music_backend directory to the path
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'python', 'music_backend'))

try:
    from downloader import search_and_download, test_environment
except ImportError as e:
    print(f"ERROR: Could not import downloader module: {e}")
    print("Make sure you're running this script from the project root directory.")
    sys.exit(1)

def setup_debug_logging():
    """Set up detailed logging for debugging."""
    log_filename = f"download_debug_{datetime.now().strftime('%Y%m%d_%H%M%S')}.log"
    
    # Create logs directory if it doesn't exist
    os.makedirs('logs', exist_ok=True)
    log_path = os.path.join('logs', log_filename)
    
    logging.basicConfig(
        level=logging.DEBUG,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        handlers=[
            logging.FileHandler(log_path),
            logging.StreamHandler(sys.stdout)
        ]
    )
    
    print(f"Debug logging enabled. Log file: {log_path}")
    return log_path

def test_basic_commands():
    """Test basic command availability."""
    print("\n=== Testing Basic Commands ===")
    
    commands_to_test = [
        ([sys.executable, '--version'], "Python"),
        ([sys.executable, '-m', 'pip', '--version'], "pip"),
        (['ffmpeg', '-version'], "FFmpeg"),
        ([sys.executable, '-m', 'spotdl', '--version'], "spotdl"),
        ([sys.executable, '-m', 'yt_dlp', '--version'], "yt-dlp")
    ]
    
    for cmd, name in commands_to_test:
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, timeout=15)
            if result.returncode == 0:
                print(f"✓ {name}: Available")
                if name == "spotdl":
                    print(f"  Version info: {result.stdout.strip()}")
            else:
                print(f"✗ {name}: Command failed (return code: {result.returncode})")
                if result.stderr:
                    print(f"  Error: {result.stderr.strip()}")
        except subprocess.TimeoutExpired:
            print(f"✗ {name}: Command timed out")
        except FileNotFoundError:
            print(f"✗ {name}: Command not found")
        except Exception as e:
            print(f"✗ {name}: Error - {e}")

def test_network_connectivity():
    """Test network connectivity to common music sources."""
    print("\n=== Testing Network Connectivity ===")
    
    urls_to_test = [
        "https://www.youtube.com",
        "https://open.spotify.com",
        "https://www.google.com"
    ]
    
    for url in urls_to_test:
        try:
            # Use curl or similar to test connectivity
            result = subprocess.run(
                ['curl', '-I', '--connect-timeout', '10', url], 
                capture_output=True, text=True, timeout=15
            )
            if result.returncode == 0:
                print(f"✓ {url}: Accessible")
            else:
                print(f"✗ {url}: Not accessible (return code: {result.returncode})")
        except FileNotFoundError:
            # Try with Python requests if curl is not available
            try:
                import requests
                response = requests.head(url, timeout=10)
                if response.status_code < 400:
                    print(f"✓ {url}: Accessible (status: {response.status_code})")
                else:
                    print(f"✗ {url}: Not accessible (status: {response.status_code})")
            except ImportError:
                print(f"? {url}: Cannot test (no curl or requests available)")
            except Exception as e:
                print(f"✗ {url}: Error - {e}")
        except Exception as e:
            print(f"✗ {url}: Error - {e}")

def test_download_with_simple_query():
    """Test download with a simple, well-known song."""
    print("\n=== Testing Download with Simple Query ===")
    
    test_queries = [
        "Never Gonna Give You Up Rick Astley",
        "Bohemian Rhapsody Queen",
        "Imagine John Lennon"
    ]
    
    for query in test_queries:
        print(f"\nTesting query: '{query}'")
        print("-" * 50)
        
        # Create a test downloads directory
        test_dir = f"./test_downloads_{datetime.now().strftime('%H%M%S')}"
        
        try:
            success = search_and_download(query, test_dir)
            
            if success:
                print(f"✓ Download reported success for: {query}")
                
                # Check if files were actually created
                if os.path.exists(test_dir):
                    files = os.listdir(test_dir)
                    audio_files = [f for f in files if f.endswith(('.mp3', '.wav', '.flac', '.m4a'))]
                    
                    if audio_files:
                        print(f"✓ Audio files created: {audio_files}")
                        for file in audio_files:
                            file_path = os.path.join(test_dir, file)
                            size = os.path.getsize(file_path)
                            print(f"  - {file}: {size} bytes")
                    else:
                        print(f"✗ No audio files found in {test_dir}")
                        print(f"  All files: {files}")
                else:
                    print(f"✗ Download directory {test_dir} was not created")
            else:
                print(f"✗ Download failed for: {query}")
                
        except Exception as e:
            print(f"✗ Exception during download test: {e}")
        
        # Clean up test directory
        try:
            if os.path.exists(test_dir):
                import shutil
                shutil.rmtree(test_dir)
                print(f"Cleaned up test directory: {test_dir}")
        except Exception as e:
            print(f"Warning: Could not clean up test directory {test_dir}: {e}")
        
        print()

def main():
    """Main debug function."""
    print("=" * 60)
    print("MUSIC DOWNLOAD DEBUG SCRIPT")
    print("=" * 60)
    
    # Set up logging
    log_path = setup_debug_logging()
    
    try:
        # Run all tests
        test_environment()
        test_basic_commands()
        test_network_connectivity()
        test_download_with_simple_query()
        
        print("\n" + "=" * 60)
        print("DEBUG COMPLETE")
        print(f"Detailed logs saved to: {log_path}")
        print("=" * 60)
        
    except KeyboardInterrupt:
        print("\nDebug interrupted by user.")
    except Exception as e:
        print(f"\nUnexpected error during debug: {e}")
        logging.exception("Unexpected error during debug")

if __name__ == "__main__":
    main()
