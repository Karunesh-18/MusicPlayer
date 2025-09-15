package com.musicplayer.util;

import jep.SharedInterpreter;
import jep.JepException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.util.HashMap;

/**
 * Optimized bridge class for Java-Python integration using JEP.
 * Maintains existing download and playback functionality while providing
 * thread-safe access and performance optimizations.
 */
public class PythonBridge {
    private SharedInterpreter interpreter;
    private final ReentrantLock interpreterLock;
    private boolean isInitialized;
    private Map<String, Object> cache;
    private static final int CACHE_SIZE_LIMIT = 100;

    public PythonBridge() {
        this.interpreterLock = new ReentrantLock();
        this.isInitialized = false;
        this.cache = new HashMap<>();
        initialize();
    }

    /**
     * Initialize the Python environment with all required modules and optimizations.
     */
    private void initialize() {
        interpreterLock.lock();
        try {
            interpreter = new SharedInterpreter();
            
            // Set up Python path and import modules
            interpreter.exec("import sys");
            interpreter.exec("import os");
            interpreter.exec("sys.path.append('python/music_backend')");
            
            // Import downloader functions
            interpreter.exec("from downloader import search_and_download, check_dependencies, get_python_executable, update_dependencies");
            
            // Import player functions
            interpreter.exec("from player import play_audio_file, find_latest_audio_file, get_audio_file_count");
            
            // Test basic functionality
            interpreter.exec("deps_ok = check_dependencies()");
            Boolean depsOk = (Boolean) interpreter.getValue("deps_ok");
            
            if (depsOk != null && depsOk) {
                isInitialized = true;
                System.out.println("🐍 PythonBridge initialized successfully");
            } else {
                System.out.println("⚠️ PythonBridge initialized with dependency warnings");
                isInitialized = true; // Still allow operation
            }
            
        } catch (JepException e) {
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
        return isInitialized && interpreter != null;
    }

    /**
     * Download a song using the Python backend with optimized performance.
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
            
            // Set the query parameter
            interpreter.set("song_query", query.trim());
            
            // Execute the download
            interpreter.exec("download_success = search_and_download(song_query)");
            
            // Get the result
            Boolean success = (Boolean) interpreter.getValue("download_success");
            boolean result = success != null && success;
            
            // Cache the result
            cacheResult(cacheKey, result);
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("🐍 Download " + (result ? "completed" : "failed") + 
                             " for '" + query + "' in " + (duration / 1000.0) + "s");
            
            return result;
            
        } catch (JepException e) {
            System.err.println("❌ Download error for '" + query + "': " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Play an audio file using the Python backend.
     */
    public boolean playAudioFile(String filePath) {
        if (!isInitialized() || filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        interpreterLock.lock();
        try {
            long startTime = System.currentTimeMillis();
            
            // Set the file path parameter
            interpreter.set("audio_file_path", filePath.trim());
            
            // Execute the playback
            interpreter.exec("play_success = play_audio_file(audio_file_path)");
            
            // Get the result
            Boolean success = (Boolean) interpreter.getValue("play_success");
            boolean result = success != null && success;
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("🐍 Playback " + (result ? "started" : "failed") + 
                             " for '" + filePath + "' in " + duration + "ms");
            
            return result;
            
        } catch (JepException e) {
            System.err.println("❌ Playback error for '" + filePath + "': " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Find the latest downloaded audio file.
     */
    public String findLatestAudioFile() {
        if (!isInitialized()) {
            return null;
        }

        // Check cache first
        String cacheKey = "latest_audio_file";
        if (cache.containsKey(cacheKey)) {
            String cachedResult = (String) cache.get(cacheKey);
            if (cachedResult != null) {
                // Verify file still exists
                if (new java.io.File(cachedResult).exists()) {
                    return cachedResult;
                } else {
                    cache.remove(cacheKey); // Remove stale cache entry
                }
            }
        }

        interpreterLock.lock();
        try {
            interpreter.exec("latest_file = find_latest_audio_file()");
            String result = (String) interpreter.getValue("latest_file");
            
            // Cache the result for a short time
            if (result != null) {
                cacheResult(cacheKey, result);
            }
            
            return result;
            
        } catch (JepException e) {
            System.err.println("❌ Error finding latest audio file: " + e.getMessage());
            return null;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Get the count of audio files in the downloads directory.
     */
    public int getAudioFileCount() {
        if (!isInitialized()) {
            return 0;
        }

        interpreterLock.lock();
        try {
            interpreter.exec("file_count = get_audio_file_count()");
            Long count = (Long) interpreter.getValue("file_count");
            return count != null ? count.intValue() : 0;
            
        } catch (JepException e) {
            System.err.println("❌ Error getting audio file count: " + e.getMessage());
            return 0;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Check if all Python dependencies are properly installed.
     */
    public boolean checkDependencies() {
        if (!isInitialized()) {
            return false;
        }

        interpreterLock.lock();
        try {
            interpreter.exec("deps_status = check_dependencies()");
            Boolean status = (Boolean) interpreter.getValue("deps_status");
            return status != null && status;
            
        } catch (JepException e) {
            System.err.println("❌ Error checking dependencies: " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Update Python dependencies for better performance and compatibility.
     */
    public boolean updateDependencies() {
        if (!isInitialized()) {
            return false;
        }

        interpreterLock.lock();
        try {
            System.out.println("🔄 Updating Python dependencies...");
            interpreter.exec("update_success = update_dependencies()");
            Boolean success = (Boolean) interpreter.getValue("update_success");
            
            boolean result = success != null && success;
            System.out.println("🐍 Dependency update " + (result ? "completed" : "failed"));
            
            return result;
            
        } catch (JepException e) {
            System.err.println("❌ Error updating dependencies: " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Get the Python executable path (cached for performance).
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

        interpreterLock.lock();
        try {
            interpreter.exec("python_exe = get_python_executable()");
            String result = (String) interpreter.getValue("python_exe");
            
            // Cache the result permanently
            if (result != null) {
                cache.put(cacheKey, result);
            }
            
            return result;
            
        } catch (JepException e) {
            System.err.println("❌ Error getting Python executable: " + e.getMessage());
            return null;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Execute custom Python code with error handling.
     */
    public boolean executeCode(String pythonCode) {
        if (!isInitialized() || pythonCode == null || pythonCode.trim().isEmpty()) {
            return false;
        }

        interpreterLock.lock();
        try {
            interpreter.exec(pythonCode);
            return true;
            
        } catch (JepException e) {
            System.err.println("❌ Error executing Python code: " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Get a value from the Python interpreter.
     */
    public Object getValue(String variableName) {
        if (!isInitialized() || variableName == null || variableName.trim().isEmpty()) {
            return null;
        }

        interpreterLock.lock();
        try {
            return interpreter.getValue(variableName);
            
        } catch (JepException e) {
            System.err.println("❌ Error getting Python value '" + variableName + "': " + e.getMessage());
            return null;
        } finally {
            interpreterLock.unlock();
        }
    }

    /**
     * Set a value in the Python interpreter.
     */
    public boolean setValue(String variableName, Object value) {
        if (!isInitialized() || variableName == null || variableName.trim().isEmpty()) {
            return false;
        }

        interpreterLock.lock();
        try {
            interpreter.set(variableName, value);
            return true;
            
        } catch (JepException e) {
            System.err.println("❌ Error setting Python value '" + variableName + "': " + e.getMessage());
            return false;
        } finally {
            interpreterLock.unlock();
        }
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
        System.out.println("🔄 Reinitializing PythonBridge...");
        
        interpreterLock.lock();
        try {
            if (interpreter != null) {
                try {
                    interpreter.close();
                } catch (Exception e) {
                    System.err.println("⚠️ Error closing old interpreter: " + e.getMessage());
                }
            }
            
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
            if (interpreter != null) {
                interpreter.close();
                interpreter = null;
            }
            isInitialized = false;
            cache.clear();
            System.out.println("🐍 PythonBridge cleaned up");
            
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
        status.put("interpreterActive", interpreter != null);
        status.put("cacheSize", cache.size());
        status.put("dependenciesOk", checkDependencies());
        status.put("audioFileCount", getAudioFileCount());
        status.put("pythonExecutable", getPythonExecutable());
        return status;
    }
}
