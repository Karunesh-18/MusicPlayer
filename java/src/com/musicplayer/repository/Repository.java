package com.musicplayer.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface defining common CRUD operations.
 * Follows the Repository pattern for data persistence abstraction.
 * 
 * @param <T> The entity type
 * @param <ID> The identifier type
 */
public interface Repository<T, ID> {
    
    /**
     * Save an entity to the repository.
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);
    
    /**
     * Save multiple entities to the repository.
     * @param entities The entities to save
     * @return The saved entities
     */
    List<T> saveAll(List<T> entities);
    
    /**
     * Find an entity by its identifier.
     * @param id The identifier
     * @return Optional containing the entity if found
     */
    Optional<T> findById(ID id);
    
    /**
     * Check if an entity exists by its identifier.
     * @param id The identifier
     * @return true if entity exists, false otherwise
     */
    boolean existsById(ID id);
    
    /**
     * Find all entities in the repository.
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Count the total number of entities.
     * @return The count of entities
     */
    long count();
    
    /**
     * Delete an entity by its identifier.
     * @param id The identifier
     * @return true if entity was deleted, false if not found
     */
    boolean deleteById(ID id);
    
    /**
     * Delete an entity.
     * @param entity The entity to delete
     * @return true if entity was deleted, false if not found
     */
    boolean delete(T entity);
    
    /**
     * Delete multiple entities.
     * @param entities The entities to delete
     * @return The number of entities deleted
     */
    int deleteAll(List<T> entities);
    
    /**
     * Delete all entities in the repository.
     * @return The number of entities deleted
     */
    int deleteAll();
    
    /**
     * Find entities by a list of identifiers.
     * @param ids The list of identifiers
     * @return List of found entities
     */
    List<T> findAllById(List<ID> ids);
    
    /**
     * Update an existing entity.
     * @param entity The entity to update
     * @return The updated entity, or null if not found
     */
    T update(T entity);
    
    /**
     * Save or update an entity (upsert operation).
     * @param entity The entity to save or update
     * @return The saved/updated entity
     */
    T saveOrUpdate(T entity);
    
    /**
     * Refresh the repository data from the underlying storage.
     * @return true if refresh was successful
     */
    boolean refresh();
    
    /**
     * Flush any pending changes to the underlying storage.
     * @return true if flush was successful
     */
    boolean flush();
    
    /**
     * Clear the repository cache (if applicable).
     */
    void clearCache();
    
    /**
     * Get repository statistics.
     * @return A map containing repository statistics
     */
    java.util.Map<String, Object> getStatistics();
}
