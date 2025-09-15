package com.musicplayer.repository;

import com.musicplayer.model.Song;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * JSON-based implementation of SongRepository.
 */
public class JsonSongRepository extends JsonRepository<Song, String> implements SongRepository {
    
    public JsonSongRepository(String filePath) {
        super(filePath, Song.class, Song::getId);
    }
    
    public JsonSongRepository() {
        this("data/songs.dat");
    }
    
    @Override
    protected Song createNewEntity() {
        return new Song();
    }
    
    @Override
    protected void validateEntity(Song entity) {
        if (entity.getTitle() == null || entity.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Song title cannot be null or empty");
        }
        if (entity.getArtist() == null || entity.getArtist().trim().isEmpty()) {
            throw new IllegalArgumentException("Song artist cannot be null or empty");
        }
    }
    
    @Override
    public List<Song> findByArtist(String artistName) {
        if (artistName == null || artistName.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(song -> 
            song.getArtist() != null && 
            song.getArtist().toLowerCase().contains(artistName.toLowerCase().trim())
        );
    }
    
    @Override
    public List<Song> findByAlbum(String albumName) {
        if (albumName == null || albumName.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(song -> 
            song.getAlbum() != null && 
            song.getAlbum().toLowerCase().contains(albumName.toLowerCase().trim())
        );
    }
    
    @Override
    public List<Song> findByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(song -> 
            song.getGenre() != null && 
            song.getGenre().equalsIgnoreCase(genre.trim())
        );
    }
    
    @Override
    public List<Song> findByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(song -> 
            song.getTitle() != null && 
            song.getTitle().toLowerCase().contains(title.toLowerCase().trim())
        );
    }
    
    @Override
    public List<Song> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return findByPredicate(song -> 
            song.getSearchableText().contains(lowerQuery)
        ).stream()
        .sorted((s1, s2) -> {
            // Prioritize exact title matches
            boolean s1TitleMatch = s1.getTitle().toLowerCase().contains(lowerQuery);
            boolean s2TitleMatch = s2.getTitle().toLowerCase().contains(lowerQuery);
            if (s1TitleMatch && !s2TitleMatch) return -1;
            if (!s1TitleMatch && s2TitleMatch) return 1;
            
            // Then by play count
            return Integer.compare(s2.getPlayCount(), s1.getPlayCount());
        })
        .collect(Collectors.toList());
    }
    
    @Override
    public List<Song> findDownloaded() {
        return findByPredicate(Song::isDownloaded);
    }
    
    @Override
    public List<Song> findLiked() {
        return findByPredicate(Song::isLiked);
    }
    
    @Override
    public List<Song> findByPlayCountGreaterThan(int minPlayCount) {
        return findByPredicate(song -> song.getPlayCount() > minPlayCount);
    }
    
    @Override
    public List<Song> findByRatingGreaterThan(double minRating) {
        return findByPredicate(song -> song.getRating() > minRating);
    }
    
    @Override
    public List<Song> findByDurationBetween(int minDuration, int maxDuration) {
        return findByPredicate(song -> 
            song.getDurationSeconds() >= minDuration && 
            song.getDurationSeconds() <= maxDuration
        );
    }
    
    @Override
    public List<String> findAllArtists() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .map(Song::getArtist)
                          .filter(artist -> artist != null && !artist.trim().isEmpty())
                          .distinct()
                          .sorted()
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<String> findAllAlbums() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .map(Song::getAlbum)
                          .filter(album -> album != null && !album.trim().isEmpty())
                          .distinct()
                          .sorted()
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<String> findAllGenres() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .map(Song::getGenre)
                          .filter(genre -> genre != null && !genre.trim().isEmpty())
                          .distinct()
                          .sorted()
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Song> findTopByPlayCount(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .sorted(Comparator.comparingInt(Song::getPlayCount).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Song> findTopByRating(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .filter(song -> song.getRating() > 0)
                          .sorted(Comparator.comparingDouble(Song::getRating).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<Song> findRecentlyAdded(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .sorted(Comparator.comparing(Song::getAddedDate).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Additional utility methods
    public long countDownloaded() {
        return countByPredicate(Song::isDownloaded);
    }
    
    public long countLiked() {
        return countByPredicate(Song::isLiked);
    }
    
    public long countByArtist(String artistName) {
        if (artistName == null || artistName.trim().isEmpty()) {
            return 0;
        }
        
        return countByPredicate(song -> 
            song.getArtist() != null && 
            song.getArtist().equalsIgnoreCase(artistName.trim())
        );
    }
    
    public long countByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return 0;
        }
        
        return countByPredicate(song -> 
            song.getGenre() != null && 
            song.getGenre().equalsIgnoreCase(genre.trim())
        );
    }
    
    public double getAverageRating() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToDouble(Song::getRating)
                          .filter(rating -> rating > 0)
                          .average()
                          .orElse(0.0);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int getTotalPlayCount() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToInt(Song::getPlayCount)
                          .sum();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public long getTotalDurationSeconds() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToLong(Song::getDurationSeconds)
                          .sum();
        } finally {
            lock.readLock().unlock();
        }
    }
}
