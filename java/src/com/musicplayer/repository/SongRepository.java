package com.musicplayer.repository;

import com.musicplayer.model.Song;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Song entities with domain-specific query methods.
 */
public interface SongRepository extends Repository<Song, String> {
    
    /**
     * Find songs by artist name.
     * @param artistName The artist name
     * @return List of songs by the artist
     */
    List<Song> findByArtist(String artistName);
    
    /**
     * Find songs by album name.
     * @param albumName The album name
     * @return List of songs in the album
     */
    List<Song> findByAlbum(String albumName);
    
    /**
     * Find songs by genre.
     * @param genre The genre
     * @return List of songs in the genre
     */
    List<Song> findByGenre(String genre);
    
    /**
     * Find songs by title (case-insensitive).
     * @param title The song title
     * @return List of songs with matching title
     */
    List<Song> findByTitle(String title);
    
    /**
     * Search songs by query (searches title, artist, album).
     * @param query The search query
     * @return List of matching songs
     */
    List<Song> search(String query);
    
    /**
     * Find downloaded songs only.
     * @return List of downloaded songs
     */
    List<Song> findDownloaded();
    
    /**
     * Find liked songs only.
     * @return List of liked songs
     */
    List<Song> findLiked();
    
    /**
     * Find songs with play count greater than specified value.
     * @param minPlayCount Minimum play count
     * @return List of popular songs
     */
    List<Song> findByPlayCountGreaterThan(int minPlayCount);
    
    /**
     * Find songs with rating greater than specified value.
     * @param minRating Minimum rating
     * @return List of highly rated songs
     */
    List<Song> findByRatingGreaterThan(double minRating);
    
    /**
     * Find songs by duration range.
     * @param minDuration Minimum duration in seconds
     * @param maxDuration Maximum duration in seconds
     * @return List of songs within duration range
     */
    List<Song> findByDurationBetween(int minDuration, int maxDuration);
    
    /**
     * Find all unique artists.
     * @return List of unique artist names
     */
    List<String> findAllArtists();
    
    /**
     * Find all unique albums.
     * @return List of unique album names
     */
    List<String> findAllAlbums();
    
    /**
     * Find all unique genres.
     * @return List of unique genres
     */
    List<String> findAllGenres();
    
    /**
     * Get songs ordered by play count (descending).
     * @param limit Maximum number of songs to return
     * @return List of most played songs
     */
    List<Song> findTopByPlayCount(int limit);
    
    /**
     * Get songs ordered by rating (descending).
     * @param limit Maximum number of songs to return
     * @return List of highest rated songs
     */
    List<Song> findTopByRating(int limit);
    
    /**
     * Get recently added songs.
     * @param limit Maximum number of songs to return
     * @return List of recently added songs
     */
    List<Song> findRecentlyAdded(int limit);
}
