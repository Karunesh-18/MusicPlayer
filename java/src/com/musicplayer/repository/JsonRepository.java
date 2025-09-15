package com.musicplayer.repository;

// Note: This implementation uses a simplified approach without external JSON dependencies
// In a real application, you would add Jackson or Gson dependencies to your project
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Abstract repository implementation providing file-based persistence.
 * Uses simple serialization for data storage with thread-safe operations.
 *
 * @param <T> The entity type
 * @param <ID> The identifier type
 */
public abstract class JsonRepository<T, ID> implements Repository<T, ID> {

    protected final String filePath;
    protected final Class<T> entityClass;
    protected final Function<T, ID> idExtractor;
    protected final ReentrantReadWriteLock lock;
    protected Map<ID, T> entities;
    protected boolean autoSave;
    protected long lastModified;

    public JsonRepository(String filePath, Class<T> entityClass, Function<T, ID> idExtractor) {
        this.filePath = filePath;
        this.entityClass = entityClass;
        this.idExtractor = idExtractor;
        this.lock = new ReentrantReadWriteLock();
        this.entities = new HashMap<>();
        this.autoSave = true;
        this.lastModified = 0;

        // Create directory if it doesn't exist
        createDirectoryIfNeeded();

        // Load existing data
        loadFromFile();
    }
    
    // Abstract methods for subclasses to implement
    protected abstract T createNewEntity();
    protected abstract void validateEntity(T entity);
    
    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        validateEntity(entity);
        
        lock.writeLock().lock();
        try {
            ID id = idExtractor.apply(entity);
            if (id == null) {
                throw new IllegalArgumentException("Entity ID cannot be null");
            }
            
            entities.put(id, entity);
            
            if (autoSave) {
                saveToFile();
            }
            
            return entity;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<T> saveAll(List<T> entitiesToSave) {
        if (entitiesToSave == null || entitiesToSave.isEmpty()) {
            return new ArrayList<>();
        }
        
        lock.writeLock().lock();
        try {
            List<T> savedEntities = new ArrayList<>();
            
            for (T entity : entitiesToSave) {
                if (entity != null) {
                    validateEntity(entity);
                    ID id = idExtractor.apply(entity);
                    if (id != null) {
                        entities.put(id, entity);
                        savedEntities.add(entity);
                    }
                }
            }
            
            if (autoSave && !savedEntities.isEmpty()) {
                saveToFile();
            }
            
            return savedEntities;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        
        lock.readLock().lock();
        try {
            return Optional.ofNullable(entities.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }
        
        lock.readLock().lock();
        try {
            return entities.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<T> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(entities.values());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public long count() {
        lock.readLock().lock();
        try {
            return entities.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public boolean deleteById(ID id) {
        if (id == null) {
            return false;
        }
        
        lock.writeLock().lock();
        try {
            boolean existed = entities.remove(id) != null;
            
            if (existed && autoSave) {
                saveToFile();
            }
            
            return existed;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean delete(T entity) {
        if (entity == null) {
            return false;
        }
        
        ID id = idExtractor.apply(entity);
        return deleteById(id);
    }
    
    @Override
    public int deleteAll(List<T> entitiesToDelete) {
        if (entitiesToDelete == null || entitiesToDelete.isEmpty()) {
            return 0;
        }
        
        lock.writeLock().lock();
        try {
            int deletedCount = 0;
            
            for (T entity : entitiesToDelete) {
                if (entity != null) {
                    ID id = idExtractor.apply(entity);
                    if (id != null && entities.remove(id) != null) {
                        deletedCount++;
                    }
                }
            }
            
            if (deletedCount > 0 && autoSave) {
                saveToFile();
            }
            
            return deletedCount;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public int deleteAll() {
        lock.writeLock().lock();
        try {
            int count = entities.size();
            entities.clear();
            
            if (count > 0 && autoSave) {
                saveToFile();
            }
            
            return count;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public List<T> findAllById(List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        
        lock.readLock().lock();
        try {
            List<T> result = new ArrayList<>();
            for (ID id : ids) {
                T entity = entities.get(id);
                if (entity != null) {
                    result.add(entity);
                }
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public T update(T entity) {
        if (entity == null) {
            return null;
        }
        
        ID id = idExtractor.apply(entity);
        if (id == null || !existsById(id)) {
            return null;
        }
        
        return save(entity);
    }
    
    @Override
    public T saveOrUpdate(T entity) {
        return save(entity); // Save handles both insert and update
    }
    
    @Override
    public boolean refresh() {
        lock.writeLock().lock();
        try {
            return loadFromFile();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean flush() {
        lock.readLock().lock();
        try {
            return saveToFile();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void clearCache() {
        lock.writeLock().lock();
        try {
            entities.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        lock.readLock().lock();
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("entityCount", entities.size());
            stats.put("filePath", filePath);
            stats.put("entityClass", entityClass.getSimpleName());
            stats.put("autoSave", autoSave);
            stats.put("lastModified", lastModified);
            stats.put("fileExists", Files.exists(Paths.get(filePath)));
            
            try {
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    stats.put("fileSize", Files.size(path));
                    stats.put("fileLastModified", Files.getLastModifiedTime(path).toMillis());
                }
            } catch (IOException e) {
                stats.put("fileError", e.getMessage());
            }
            
            return stats;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // File operations using simple serialization
    protected boolean loadFromFile() {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                // Create empty file
                entities.clear();
                saveToFile();
                return true;
            }

            // Use Java serialization for simplicity
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
                @SuppressWarnings("unchecked")
                Map<ID, T> loadedEntities = (Map<ID, T>) ois.readObject();
                entities.clear();
                entities.putAll(loadedEntities);

                lastModified = Files.getLastModifiedTime(path).toMillis();
                return true;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Failed to load from file " + filePath + ": " + e.getMessage());
            // Try to create empty file on error
            entities.clear();
            return saveToFile();
        }
    }

    protected boolean saveToFile() {
        try {
            Path path = Paths.get(filePath);

            // Use Java serialization for simplicity
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
                oos.writeObject(new HashMap<>(entities));

                lastModified = System.currentTimeMillis();
                return true;
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to save to file " + filePath + ": " + e.getMessage());
            return false;
        }
    }
    
    protected void createDirectoryIfNeeded() {
        try {
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to create directory for " + filePath + ": " + e.getMessage());
        }
    }
    
    // Configuration methods
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }
    
    public boolean isAutoSave() {
        return autoSave;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public long getLastModified() {
        return lastModified;
    }
    
    // Utility methods for subclasses
    protected List<T> findByPredicate(java.util.function.Predicate<T> predicate) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .filter(predicate)
                          .collect(java.util.stream.Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    protected Optional<T> findFirstByPredicate(java.util.function.Predicate<T> predicate) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .filter(predicate)
                          .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    protected long countByPredicate(java.util.function.Predicate<T> predicate) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .filter(predicate)
                          .count();
        } finally {
            lock.readLock().unlock();
        }
    }
}
