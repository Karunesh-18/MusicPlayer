# Download Debugging Guide

This guide explains how to debug download issues in the MusicPlayer application.

## Quick Debug Steps

### 1. Run the Download Test Script
```powershell
.\test_download.ps1
```
This script will test all download-related components and provide detailed feedback.

### 2. Run the Comprehensive Debug Script
```powershell
python debug_download.py
```
This Python script provides the most detailed debugging information and creates log files.

### 3. Test Individual Components
```powershell
.\test_components.ps1
```
This tests all application components including the enhanced download debugging.

## Understanding Debug Output

### Success Indicators
- ✓ Green checkmarks indicate successful tests
- "OK" messages in green indicate passing tests
- Log files in the `logs/` directory contain detailed information

### Failure Indicators
- ✗ Red X marks indicate failed tests
- "FAIL" messages in red indicate failing tests
- Error messages provide specific failure reasons

## Common Issues and Solutions

### 1. "spotdl: error: unrecognized arguments: --verbose" (FIXED)
**Issue:** The original code used an invalid `--verbose` flag and missing `download` subcommand
**Solution:** Updated to use correct spotdl command format:
```bash
python -m spotdl download "Song Name" --output ./downloads --format mp3 --print-errors
```

### 2. "spotdl command not found"
**Solution:** Install spotdl
```bash
pip install spotdl
```

### 3. "FFmpeg not found" (Warning, not blocking)
**Solution:** Install FFmpeg and add to PATH (optional for basic downloads)
- Download from https://ffmpeg.org/
- Add the bin directory to your system PATH
- Note: Downloads may still work without FFmpeg for some formats

### 4. "Network connection issues"
**Solution:** Check internet connection and firewall settings
- Ensure YouTube and Spotify are accessible
- Check if your firewall is blocking Python/Java applications

### 5. "No results found"
**Solution:** Try different search terms
- Include artist name: "Song Title Artist Name"
- Use more specific terms
- Try popular, well-known songs first

### 6. "Download reported success but no files found"
**Solution:** Check permissions and disk space
- Ensure the downloads directory is writable
- Check available disk space
- Verify the output directory path

## Debug Features Added

### Enhanced Logging
- All download operations now log detailed information
- Log files are created in the `logs/` directory with timestamps
- Both console and file logging are enabled

### Verbose Output
- Added `--verbose` flag to spotdl commands
- Detailed command execution information
- Step-by-step progress reporting

### Environment Testing
- Python version and executable path
- Module import verification
- Network connectivity tests
- Command availability checks

### Error Classification
- Specific error messages for common issues
- Hints and suggestions for resolution
- Detailed error context and debugging information

## Debug Log Files

Debug logs are saved in the `logs/` directory with filenames like:
- `download_debug_YYYYMMDD_HHMMSS.log`

These files contain:
- Complete command execution details
- Network request information
- File system operations
- Error stack traces
- Environment information

## Manual Testing Commands

### Test spotdl directly:
```bash
python -m spotdl "Never Gonna Give You Up Rick Astley" --output ./test_downloads --format mp3 --verbose
```

### Test yt-dlp directly:
```bash
python -m yt_dlp "https://www.youtube.com/watch?v=dQw4w9WgXcQ" -o "./test_downloads/%(title)s.%(ext)s"
```

### Test Python imports:
```bash
python -c "import spotdl, yt_dlp, dotenv; print('All imports successful')"
```

## Getting Help

If downloads are still failing after running these debug scripts:

1. **Check the log files** in the `logs/` directory for detailed error information
2. **Run the debug scripts** and share the output when asking for help
3. **Test individual components** to isolate the issue
4. **Verify your environment** meets all the requirements listed in README.md

## Environment Requirements

Ensure you have:
- Python 3.8+
- Java 17+
- FFmpeg installed and in PATH
- Internet connection
- Required Python packages: `pip install spotdl yt-dlp python-dotenv jep`

## Debug Script Options

### test_download.ps1
- Quick component testing
- Network connectivity checks
- Basic download functionality test

### debug_download.py
- Comprehensive environment analysis
- Detailed logging
- Multiple test scenarios
- Log file generation

### test_components.ps1
- Full application component testing
- Java-Python integration testing
- Enhanced with download debugging
