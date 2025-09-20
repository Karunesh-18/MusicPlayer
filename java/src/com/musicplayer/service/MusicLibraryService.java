package com.musicplayer.service;

import com.musicplayer.model.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

// Manages the music collection - searching, organizing, etc
public class MusicLibraryService {
    private Map<String, Song> songs;
    private Map<String, Artist> artists;
    private Map<String, Album> albums;
    private Map<String, List<Song>> genreIndex;
    private Map<String, List<Song>> artistIndex;
    private Map<String, List<Song>> albumIndex;

    public MusicLibraryService() {
        this.songs = new HashMap<>();
        this.artists = new HashMap<>();
        this.albums = new HashMap<>();
        this.genreIndex = new HashMap<>();
        this.artistIndex = new HashMap<>();
        this.albumIndex = new HashMap<>();
    }

    // Load music files from a folder
    public int loadFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }

        File[] audioFiles = directory.listFiles((dir, name) ->
            name.toLowerCase().endsWith(".mp3") ||
            name.toLowerCase().endsWith(".wav") ||
            name.toLowerCase().endsWith(".m4a"));

        if (audioFiles == null) {
            return 0;
        }

        int loadedCount = 0;
        for (File file : audioFiles) {
            try {
                Song song = createSongFromFile(file);
                if (song != null) {
                    addSong(song);
                    loadedCount++;
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error loading file " + file.getName() + ": " + e.getMessage());
            }
        }

        return loadedCount;
    }

    /**
     * Create a song from a file
     */
    private Song createSongFromFile(File file) {
        String fileName = file.getName();
        String[] parts = fileName.replace(".mp3", "").replace(".wav", "").replace(".m4a", "").split(" - ", 2);

        String artist = parts.length > 1 ? parts[0] : "Unknown Artist";
        String title = parts.length > 1 ? parts[1] : fileName.substring(0, fileName.lastIndexOf('.'));

        Song song = new Song(title, artist);
        song.setFilePath(file.getAbsolutePath());
        return song;
    }

    /**
     * Add a song from a file path with original query
     */
    public Song addSongFromFile(String filePath, String originalQuery) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        Song song = createSongFromFile(file);
        if (song != null) {
            addSong(song);
        }

        return song;
    }

    /**
     * Get recommendations for a user based on listening history
     */
    public List<Song> getRecommendationsForUser(User user) {
        List<Song> recentlyPlayed = user.getRecentlyPlayed();
        if (recentlyPlayed.isEmpty()) {
            return getMostPlayedSongs(10);
        }

        // Simple recommendation: songs by same artists as recently played
        Set<String> preferredArtists = recentlyPlayed.stream()
            .map(Song::getArtist)
            .collect(Collectors.toSet());

        return songs.values().stream()
            .filter(song -> preferredArtists.contains(song.getArtist()))
            .filter(song -> !recentlyPlayed.contains(song))
            .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
            .limit(10)
            .collect(Collectors.toList());
    }

    // Adding songs to the library
    public void addSong(Song song) {
        if (song != null && song.getId() != null) {
            songs.put(song.getId(), song);
            updateIndices(song);

            // Make sure we have the artist info too
            Artist artist = getOrCreateArtist(song.getArtist());
            artist.addSong(song);

            // And album info if we have it
            if (song.getAlbum() != null && !song.getAlbum().isEmpty()) {
                Album album = getOrCreateAlbum(song.getAlbum(), artist);
                album.addTrack(song);
            }
        }
    }

    public void removeSong(String songId) {
        Song song = songs.remove(songId);
        if (song != null) {
            removeFromIndices(song);
            
            // Update artist
            Artist artist = artists.get(song.getArtist().toLowerCase());
            if (artist != null) {
                artist.removeSong(song);
            }
        }
    }

    public Song getSongById(String id) {
        return songs.get(id);
    }

    public List<Song> getAllSongs() {
        return new ArrayList<>(songs.values());
    }

    // Finding songs by search terms
    public List<Song> searchSongs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase();
        return songs.values().stream()
                   .filter(song -> song.getSearchableText().contains(lowerQuery))
                   .sorted((s1, s2) -> {
                       // Put exact title matches first
                       boolean s1TitleMatch = s1.getTitle().toLowerCase().contains(lowerQuery);
                       boolean s2TitleMatch = s2.getTitle().toLowerCase().contains(lowerQuery);
                       if (s1TitleMatch && !s2TitleMatch) return -1;
                       if (!s1TitleMatch && s2TitleMatch) return 1;

                       // Then sort by how popular they are
                       return Integer.compare(s2.getPlayCount(), s1.getPlayCount());
                   })
                   .collect(Collectors.toList());
    }

    public List<Artist> searchArtists(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        return artists.values().stream()
                     .filter(artist -> artist.getSearchableText().contains(lowerQuery))
                     .sorted((a1, a2) -> {
                         // Prioritize exact name matches
                         boolean a1NameMatch = a1.getName().toLowerCase().contains(lowerQuery);
                         boolean a2NameMatch = a2.getName().toLowerCase().contains(lowerQuery);
                         if (a1NameMatch && !a2NameMatch) return -1;
                         if (!a1NameMatch && a2NameMatch) return 1;
                         
                         // Then by popularity
                         return Double.compare(a2.getPopularity(), a1.getPopularity());
                     })
                     .collect(Collectors.toList());
    }

    public List<Album> searchAlbums(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        return albums.values().stream()
                    .filter(album -> album.getSearchableText().contains(lowerQuery))
                    .sorted((a1, a2) -> {
                        // Prioritize exact title matches
                        boolean a1TitleMatch = a1.getTitle().toLowerCase().contains(lowerQuery);
                        boolean a2TitleMatch = a2.getTitle().toLowerCase().contains(lowerQuery);
                        if (a1TitleMatch && !a2TitleMatch) return -1;
                        if (!a1TitleMatch && a2TitleMatch) return 1;
                        
                        // Then by total play count
                        return Integer.compare(a2.getTotalPlayCount(), a1.getTotalPlayCount());
                    })
                    .collect(Collectors.toList());
    }

    // Advanced Search with Filters
    public List<Song> searchSongsWithFilters(String query, String genre, String artist, 
                                           Integer minDuration, Integer maxDuration) {
        List<Song> results = query != null ? searchSongs(query) : getAllSongs();
        
        return results.stream()
                     .filter(song -> genre == null || genre.equalsIgnoreCase(song.getGenre()))
                     .filter(song -> artist == null || song.getArtist().toLowerCase().contains(artist.toLowerCase()))
                     .filter(song -> minDuration == null || song.getDurationSeconds() >= minDuration)
                     .filter(song -> maxDuration == null || song.getDurationSeconds() <= maxDuration)
                     .collect(Collectors.toList());
    }

    // Recommendations
    public List<Song> getRecommendationsFor(User user) {
        if (user == null) return new ArrayList<>();
        
        List<Song> recommendations = new ArrayList<>();
        
        // Get user's top genres
        Map<String, Long> topGenres = user.getTopGenres();
        
        // Recommend popular songs from user's favorite genres
        for (String genre : topGenres.keySet()) {
            List<Song> genreSongs = getSongsByGenre(genre);
            genreSongs.stream()
                     .filter(song -> !user.getLikedSongs().contains(song))
                     .filter(Song::isPopular)
                     .limit(5)
                     .forEach(recommendations::add);
        }
        
        // Add trending songs
        recommendations.addAll(getTrendingSongs(10));
        
        // Remove duplicates and limit
        return recommendations.stream()
                            .distinct()
                            .limit(20)
                            .collect(Collectors.toList());
    }

    public List<Song> getSimilarSongs(Song song) {
        if (song == null) return new ArrayList<>();
        
        return songs.values().stream()
                   .filter(s -> !s.equals(song))
                   .filter(s -> s.getArtist().equalsIgnoreCase(song.getArtist()) ||
                              (s.getGenre() != null && s.getGenre().equalsIgnoreCase(song.getGenre())))
                   .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                   .limit(10)
                   .collect(Collectors.toList());
    }

    // Trending and Popular Content
    public List<Song> getTrendingSongs(int limit) {
        return songs.values().stream()
                   .sorted((s1, s2) -> {
                       // Sort by recent popularity (play count + recency)
                       double s1Score = s1.getPlayCount() + (s1.isLiked() ? 5 : 0);
                       double s2Score = s2.getPlayCount() + (s2.isLiked() ? 5 : 0);
                       return Double.compare(s2Score, s1Score);
                   })
                   .limit(limit)
                   .collect(Collectors.toList());
    }

    public List<Artist> getTrendingArtists(int limit) {
        return artists.values().stream()
                     .sorted((a1, a2) -> Double.compare(a2.getPopularity(), a1.getPopularity()))
                     .limit(limit)
                     .collect(Collectors.toList());
    }

    public List<Album> getTrendingAlbums(int limit) {
        return albums.values().stream()
                    .sorted((a1, a2) -> Integer.compare(a2.getTotalPlayCount(), a1.getTotalPlayCount()))
                    .limit(limit)
                    .collect(Collectors.toList());
    }

    // Genre-based Operations
    public List<Song> getSongsByGenre(String genre) {
        return genreIndex.getOrDefault(genre.toLowerCase(), new ArrayList<>());
    }

    public List<String> getAllGenres() {
        return new ArrayList<>(genreIndex.keySet());
    }

    public Map<String, Integer> getGenreStatistics() {
        return genreIndex.entrySet().stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().size()
                        ));
    }

    // Artist Operations
    public Artist getArtistByName(String name) {
        return artists.get(name.toLowerCase());
    }

    public List<Artist> getAllArtists() {
        return new ArrayList<>(artists.values());
    }

    public List<Song> getSongsByArtist(String artistName) {
        return artistIndex.getOrDefault(artistName.toLowerCase(), new ArrayList<>());
    }

    // Album Operations
    public Album getAlbumByTitle(String title, String artistName) {
        String key = (title + "_" + artistName).toLowerCase();
        return albums.get(key);
    }

    public List<Album> getAllAlbums() {
        return new ArrayList<>(albums.values());
    }

    public List<Song> getSongsByAlbum(String albumTitle) {
        return albumIndex.getOrDefault(albumTitle.toLowerCase(), new ArrayList<>());
    }

    // Statistics
    public int getTotalSongs() {
        return songs.size();
    }

    public int getTotalArtists() {
        return artists.size();
    }

    public int getTotalAlbums() {
        return albums.size();
    }

    public Map<String, Object> getLibraryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSongs", getTotalSongs());
        stats.put("totalArtists", getTotalArtists());
        stats.put("totalAlbums", getTotalAlbums());
        stats.put("totalGenres", genreIndex.size());
        stats.put("totalPlayCount", songs.values().stream().mapToInt(Song::getPlayCount).sum());
        stats.put("averageRating", songs.values().stream().mapToDouble(Song::getRating).filter(r -> r > 0).average().orElse(0.0));
        return stats;
    }

    // Private Helper Methods
    private Artist getOrCreateArtist(String artistName) {
        final String finalArtistName = (artistName == null || artistName.trim().isEmpty()) ?
            "Unknown Artist" : artistName;

        String key = finalArtistName.toLowerCase();
        return artists.computeIfAbsent(key, k -> new Artist(finalArtistName));
    }

    private Album getOrCreateAlbum(String albumTitle, Artist artist) {
        String key = (albumTitle + "_" + artist.getName()).toLowerCase();
        return albums.computeIfAbsent(key, k -> new Album(albumTitle, artist));
    }

    private void updateIndices(Song song) {
        // Genre index
        if (song.getGenre() != null) {
            genreIndex.computeIfAbsent(song.getGenre().toLowerCase(), k -> new ArrayList<>()).add(song);
        }
        
        // Artist index
        if (song.getArtist() != null) {
            artistIndex.computeIfAbsent(song.getArtist().toLowerCase(), k -> new ArrayList<>()).add(song);
        }
        
        // Album index
        if (song.getAlbum() != null) {
            albumIndex.computeIfAbsent(song.getAlbum().toLowerCase(), k -> new ArrayList<>()).add(song);
        }
    }

    private void removeFromIndices(Song song) {
        // Remove from genre index
        if (song.getGenre() != null) {
            List<Song> genreSongs = genreIndex.get(song.getGenre().toLowerCase());
            if (genreSongs != null) {
                genreSongs.remove(song);
                if (genreSongs.isEmpty()) {
                    genreIndex.remove(song.getGenre().toLowerCase());
                }
            }
        }
        
        // Remove from artist index
        if (song.getArtist() != null) {
            List<Song> artistSongs = artistIndex.get(song.getArtist().toLowerCase());
            if (artistSongs != null) {
                artistSongs.remove(song);
                if (artistSongs.isEmpty()) {
                    artistIndex.remove(song.getArtist().toLowerCase());
                }
            }
        }
        
        // Remove from album index
        if (song.getAlbum() != null) {
            List<Song> albumSongs = albumIndex.get(song.getAlbum().toLowerCase());
            if (albumSongs != null) {
                albumSongs.remove(song);
                if (albumSongs.isEmpty()) {
                    albumIndex.remove(song.getAlbum().toLowerCase());
                }
            }
        }
    }

    // Additional methods for UI compatibility
    public List<Song> getMostPlayedSongs(int limit) {
        return songs.values().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Song> getRecentlyAddedSongs(int limit) {
        // Sort by song ID (assuming newer songs have higher IDs) or just return first N songs
        return songs.values().stream()
                .sorted((s1, s2) -> s2.getId().compareTo(s1.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Song> getDownloadedSongs() {
        return songs.values().stream()
                .filter(song -> song.getFilePath() != null && !song.getFilePath().isEmpty())
                .collect(Collectors.toList());
    }
}
