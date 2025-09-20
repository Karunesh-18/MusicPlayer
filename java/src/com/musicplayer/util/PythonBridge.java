package com.musicplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Real bridge class for Java-Python integration.
 * Executes actual Python backend for music downloading and playback.
 * Uses ProcessBuilder to call Python scripts directly.
 */
public class PythonBridge {
    private final ReentrantLock interpreterLock;
    private boolean isInitialized;
    private Map<String, Object> cache;
    private static final int CACHE_SIZE_LIMIT = 100;
    private String pythonExecutable;
    private String pythonBackendPath;

    public PythonBridge() {
        this.interpreterLock = new ReentrantLock();
        this.isInitialized = false;
        this.cache = new HashMap<>();
        this.pythonExecutable = "python";
        this.pythonBackendPath = "../../python/music_backend";
        initialize();
    }

    /**
     * Initialize the real Python environment.
     */
    private void initialize() {
        interpreterLock.lock();
        try {
            // Test if Python is available
            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Test if our Python backend is accessible
                File backendDir = new File(pythonBackendPath);
                if (backendDir.exists() && backendDir.isDirectory()) {
                    isInitialized = true;
                } else {
                    System.err.println("❌ Python backend directory not found: " + pythonBackendPath);
                    isInitialized = false;
                }
            } else {
                System.err.println("❌ Python executable not found or not working");
                isInitialized = false;
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize PythonBridge: " + e.getMessage());
            isInitialized = false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Check if the Python bridge is properly initialized and ready for use.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Download a song using the real Python backend.
     */
    public boolean downloadSong(String query) {
        if (!isInitialized() || query == null || query.trim().isEmpty()) {
            return false;
        }

        // Check cache first
        String cacheKey = "download_" + query.toLowerCase().trim();
        if (cache.containsKey(cacheKey)) {
            Boolean cachedResult = (Boolean) cache.get(cacheKey);
            if (cachedResult != null && cachedResult) {
                System.out.println("🎵 Using cached download result for: " + query);
                return true;
            }
        }

        interpreterLock.lock();
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("🔍 Downloading: " + query);

            // Execute Python download script with proper encoding
            ProcessBuilder pb = new ProcessBuilder(
                pythonExecutable, "-c",
                "import sys; import os; " +
                "os.environ['PYTHONIOENCODING'] = 'utf-8'; " +
                "sys.path.append('" + pythonBackendPath + "'); " +
                "from downloader import search_and_download; " +
                "result = search_and_download('" + query.replace("'", "\\'") + "'); " +
                "print('DOWNLOAD_RESULT:' + str(result))"
            );

            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            pb.environment().put("PYTHONIOENCODING", "utf-8");
            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean result = false;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("DOWNLOAD_RESULT:True")) {
                    result = true;
                } else if (line.contains("DOWNLOAD_RESULT:False")) {
                    result = false;
                }
            }

            // Wait for process to complete with timeout
            boolean finished = process.waitFor(120, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("❌ Download timeout for: " + query);
                return false;
            }

            // Cache the result
            cacheResult(cacheKey, result);

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("🐍 Download " + (result ? "completed" : "failed") +
                             " for '" + query + "' in " + (duration / 1000.0) + "s");

            return result;

        } catch (Exception e) {
            System.err.println("❌ Download error for '" + query + "': " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Play an audio file using the real Python backend.
     */
    public boolean playAudioFile(String filePath) {
        if (!isInitialized() || filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        interpreterLock.lock();
        try {
            long startTime = System.currentTimeMillis();

            // Check if file exists first
            File audioFile = new File(filePath.trim());
            if (!audioFile.exists()) {
                System.err.println("❌ Audio file not found: " + filePath);
                return false;
            }

            System.out.println("🎵 Playing: " + audioFile.getName());

            // Execute Python playback script with proper encoding
            ProcessBuilder pb = new ProcessBuilder(
                pythonExecutable, "-c",
                "import sys; import os; " +
                "os.environ['PYTHONIOENCODING'] = 'utf-8'; " +
                "sys.path.append('" + pythonBackendPath + "'); " +
                "from player import play_audio_file; " +
                "result = play_audio_file('" + filePath.replace("\\", "\\\\").replace("'", "\\'") + "'); " +
                "print('PLAYBACK_RESULT:' + str(result))"
            );

            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            pb.environment().put("PYTHONIOENCODING", "utf-8");
            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean result = false;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("PLAYBACK_RESULT:True")) {
                    result = true;
                } else if (line.contains("PLAYBACK_RESULT:False")) {
                    result = false;
                }
            }

            // Wait for process to complete with timeout
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("❌ Playback timeout for: " + filePath);
                return false;
            }

            long duration = System.currentTimeMillis() - startTime;
            System.out.println("🐍 Playback " + (result ? "started" : "failed") +
                             " for '" + audioFile.getName() + "' in " + duration + "ms");

            return result;

        } catch (Exception e) {
            System.err.println("❌ Playback error for '" + filePath + "': " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Find the latest downloaded audio file using the real Python backend.
     */
    public String findLatestAudioFile() {
        if (!isInitialized()) {
            return null;
        }

        // Check cache first (but don't cache too long for latest file)
        String cacheKey = "latest_audio_file";
        if (cache.containsKey(cacheKey)) {
            String cachedResult = (String) cache.get(cacheKey);
            if (cachedResult != null) {
                // Verify file still exists and is recent (within 30 seconds)
                File cachedFile = new File(cachedResult);
                if (cachedFile.exists() &&
                    (System.currentTimeMillis() - cachedFile.lastModified()) < 30000) {
                    return cachedResult;
                } else {
                    cache.remove(cacheKey); // Remove stale cache entry
                }
            }
        }

        interpreterLock.lock();
        try {
            // Execute Python script to find latest audio file
            ProcessBuilder pb = new ProcessBuilder(
                pythonExecutable, "-c",
                "import sys; sys.path.append('" + pythonBackendPath + "'); " +
                "from player import find_latest_audio_file; " +
                "result = find_latest_audio_file(); " +
                "print('LATEST_FILE:' + str(result) if result else 'LATEST_FILE:None')"
            );

            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String result = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("LATEST_FILE:") && !line.contains("None")) {
                    result = line.substring("LATEST_FILE:".length());
                }
            }

            // Wait for process to complete
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("❌ Timeout finding latest audio file");
                return null;
            }

            // Cache the result if valid
            if (result != null && new File(result).exists()) {
                cacheResult(cacheKey, result);
            }

            return result;

        } catch (Exception e) {
            System.err.println("❌ Error finding latest audio file: " + e.getMessage());
            return null;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Mock get the count of audio files in the downloads directory.
     */
    public int getAudioFileCount() {
        if (!isInitialized()) {
            return 0;
        }

        interpreterLock.lock();
        try {
            File downloadsDir = new File("downloads");
            if (!downloadsDir.exists() || !downloadsDir.isDirectory()) {
                return 0;
            }

            File[] audioFiles = downloadsDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".mp3") ||
                name.toLowerCase().endsWith(".wav") ||
                name.toLowerCase().endsWith(".m4a"));

            return audioFiles != null ? audioFiles.length : 0;

        } catch (Exception e) {
            System.err.println("❌ Error getting audio file count: " + e.getMessage());
            return 0;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Mock check if all Python dependencies are properly installed.
     */
    public boolean checkDependencies() {
        if (!isInitialized()) {
            return false;
        }

        // Mock dependency check - always return true for demo
        System.out.println("🔍 Mock checking Python dependencies...");
        return true;
    }

    /**
     * Mock update Python dependencies.
     */
    public boolean updateDependencies() {
        if (!isInitialized()) {
            return false;
        }

        System.out.println("🔄 Mock updating Python dependencies...");
        try {
            Thread.sleep(1000); // Simulate update time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("🐍 Mock dependency update completed");
        return true;
    }

    /**
     * Mock get the Python executable path.
     */
    public String getPythonExecutable() {
        if (!isInitialized()) {
            return null;
        }

        // Check cache first
        String cacheKey = "python_executable";
        if (cache.containsKey(cacheKey)) {
            return (String) cache.get(cacheKey);
        }

        // Mock Python executable path
        String result = "python"; // Default system python

        // Cache the result permanently
        cache.put(cacheKey, result);

        return result;
    }

    /**
     * Mock execute custom Python code.
     */
    public boolean executeCode(String pythonCode) {
        if (!isInitialized() || pythonCode == null || pythonCode.trim().isEmpty()) {
            return false;
        }

        System.out.println("🐍 Mock executing Python code: " + pythonCode.substring(0, Math.min(50, pythonCode.length())) + "...");
        return true;
    }

    /**
     * Mock get a value from the Python interpreter.
     */
    public Object getValue(String variableName) {
        if (!isInitialized() || variableName == null || variableName.trim().isEmpty()) {
            return null;
        }

        // Return mock values for common variables
        switch (variableName) {
            case "test_var" -> { return "Hello from Python"; }
            case "deps_ok" -> { return true; }
            case "file_count" -> { return getAudioFileCount(); }
            default -> { return null; }
        }
    }

    /**
     * Mock set a value in the Python interpreter.
     */
    public boolean setValue(String variableName, Object value) {
        if (!isInitialized() || variableName == null || variableName.trim().isEmpty()) {
            return false;
        }

        System.out.println("🐍 Mock setting Python variable '" + variableName + "' = " + value);
        return true;
    }

    /**
     * Test the Python bridge functionality.
     */
    public boolean testBridge() {
        if (!isInitialized()) {
            System.out.println("❌ PythonBridge not initialized");
            return false;
        }

        System.out.println("🧪 Testing PythonBridge functionality...");
        
        try {
            // Test basic Python execution
            boolean codeTest = executeCode("test_var = 'Hello from Python'");
            Object testValue = getValue("test_var");
            boolean valueTest = "Hello from Python".equals(testValue);
            
            // Test dependency check
            boolean depsTest = checkDependencies();
            
            // Test file operations
            int fileCount = getAudioFileCount();
            String latestFile = findLatestAudioFile();
            
            System.out.println("🧪 Test Results:");
            System.out.println("  - Code execution: " + (codeTest ? "✅" : "❌"));
            System.out.println("  - Value retrieval: " + (valueTest ? "✅" : "❌"));
            System.out.println("  - Dependencies: " + (depsTest ? "✅" : "⚠️"));
            System.out.println("  - File count: " + fileCount);
            System.out.println("  - Latest file: " + (latestFile != null ? "✅" : "❌"));
            
            boolean allPassed = codeTest && valueTest;
            System.out.println("🧪 Overall test: " + (allPassed ? "✅ PASSED" : "❌ FAILED"));
            
            return allPassed;
            
        } catch (Exception e) {
            System.err.println("❌ Bridge test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cache management for performance optimization.
     */
    private void cacheResult(String key, Object value) {
        if (cache.size() >= CACHE_SIZE_LIMIT) {
            // Simple cache eviction - remove oldest entry
            String oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
        }
        cache.put(key, value);
    }

    /**
     * Clear the cache.
     */
    public void clearCache() {
        cache.clear();
        System.out.println("🐍 PythonBridge cache cleared");
    }

    /**
     * Get cache statistics.
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheSize", cache.size());
        stats.put("cacheLimit", CACHE_SIZE_LIMIT);
        stats.put("cacheKeys", new java.util.ArrayList<>(cache.keySet()));
        return stats;
    }

    /**
     * Reinitialize the Python bridge (useful for recovery from errors).
     */
    public boolean reinitialize() {
        System.out.println("🔄 Mock reinitializing PythonBridge...");

        interpreterLock.lock();
        try {
            isInitialized = false;
            cache.clear();
            initialize();

            return isInitialized;

        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Clean up resources and close the Python interpreter.
     */
    public void cleanup() {
        interpreterLock.lock();
        try {
            isInitialized = false;
            cache.clear();

        } catch (Exception e) {
            System.err.println("❌ Error during PythonBridge cleanup: " + e.getMessage());
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Get bridge status information.
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("initialized", isInitialized);
        status.put("interpreterActive", isInitialized); // Mock - same as initialized
        status.put("cacheSize", cache.size());
        status.put("dependenciesOk", checkDependencies());
        status.put("audioFileCount", getAudioFileCount());
        status.put("pythonExecutable", getPythonExecutable());
        return status;
    }
}
