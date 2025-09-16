package com.musicplayer.service;

import com.musicplayer.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for comprehensive playlist management including creation,
 * modification, sharing, and collaborative features.
 */
public class PlaylistService {
    private Map<String, Playlist> playlists;
    private Map<String, List<Playlist>> userPlaylistIndex;
    private Map<String, List<Playlist>> publicPlaylistIndex;

    public PlaylistService() {
        this.playlists = new HashMap<>();
        this.userPlaylistIndex = new HashMap<>();
        this.publicPlaylistIndex = new HashMap<>();
    }

    // Playlist CRUD Operations
    public Playlist createPlaylist(String name, User owner) {
        if (name == null || name.trim().isEmpty() || owner == null) {
            throw new IllegalArgumentException("Playlist name and owner cannot be null or empty");
        }
        
        Playlist playlist = new Playlist(name.trim(), owner);
        playlists.put(playlist.getId(), playlist);
        
        // Update indices
        userPlaylistIndex.computeIfAbsent(owner.getId(), k -> new ArrayList<>()).add(playlist);
        
        return playlist;
    }

    public Playlist createPlaylist(String name, User owner, boolean isPublic) {
        Playlist playlist = createPlaylist(name, owner);
        playlist.setPublic(isPublic);
        
        if (isPublic) {
            publicPlaylistIndex.computeIfAbsent("public", k -> new ArrayList<>()).add(playlist);
        }
        
        return playlist;
    }

    public boolean deletePlaylist(String playlistId, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.getOwner().equals(user)) {
            return false;
        }
        
        // Don't allow deletion of system playlists
        if (playlist.getType() != Playlist.PlaylistType.USER_CREATED) {
            return false;
        }
        
        // Remove from indices
        removeFromIndices(playlist);
        
        return playlists.remove(playlistId) != null;
    }

    public Playlist getPlaylistById(String id) {
        return playlists.get(id);
    }

    public Optional<Playlist> getPlaylistByIdOptional(String id) {
        return Optional.ofNullable(playlists.get(id));
    }

    public boolean updatePlaylist(String playlistId, User user, String newName, String newDescription) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.canEdit(user)) {
            return false;
        }
        
        if (newName != null && !newName.trim().isEmpty()) {
            playlist.setName(newName.trim());
        }
        
        if (newDescription != null) {
            playlist.setDescription(newDescription.trim());
        }
        
        return true;
    }

    // Song Management in Playlists
    public boolean addSongToPlaylist(String playlistId, Song song, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || song == null || !playlist.canEdit(user)) {
            return false;
        }
        
        playlist.addSong(song);
        return true;
    }

    public boolean addSongToPlaylistAtPosition(String playlistId, Song song, int position, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || song == null || !playlist.canEdit(user)) {
            return false;
        }
        
        playlist.addSongAtPosition(song, position);
        return true;
    }

    public boolean removeSongFromPlaylist(String playlistId, Song song, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || song == null || !playlist.canEdit(user)) {
            return false;
        }
        
        return playlist.removeSong(song);
    }

    public boolean moveSongInPlaylist(String playlistId, int fromPosition, int toPosition, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.canEdit(user)) {
            return false;
        }
        
        playlist.moveSong(fromPosition, toPosition);
        return true;
    }

    // Playlist Discovery
    public List<Playlist> getUserPlaylists(User user) {
        if (user == null) return new ArrayList<>();
        return userPlaylistIndex.getOrDefault(user.getId(), new ArrayList<>());
    }

    public List<Playlist> getPublicPlaylists() {
        return publicPlaylistIndex.getOrDefault("public", new ArrayList<>());
    }

    public List<Playlist> getPublicPlaylists(int limit) {
        return getPublicPlaylists().stream()
                                  .sorted((p1, p2) -> p2.getFollowers().size() - p1.getFollowers().size())
                                  .limit(limit)
                                  .collect(Collectors.toList());
    }

    public List<Playlist> searchPlaylists(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        return getPublicPlaylists().stream()
                                  .filter(playlist -> playlist.getSearchableText().contains(lowerQuery))
                                  .sorted((p1, p2) -> {
                                      // Prioritize exact name matches
                                      boolean p1NameMatch = p1.getName().toLowerCase().contains(lowerQuery);
                                      boolean p2NameMatch = p2.getName().toLowerCase().contains(lowerQuery);
                                      if (p1NameMatch && !p2NameMatch) return -1;
                                      if (!p1NameMatch && p2NameMatch) return 1;
                                      
                                      // Then by follower count
                                      return p2.getFollowers().size() - p1.getFollowers().size();
                                  })
                                  .collect(Collectors.toList());
    }

    public List<Playlist> getPlaylistsByGenre(String genre) {
        return getPublicPlaylists().stream()
                                  .filter(playlist -> {
                                      Map<String, Long> genreDistribution = playlist.getGenreDistribution();
                                      return genreDistribution.containsKey(genre) && 
                                             genreDistribution.get(genre) > playlist.size() * 0.3; // At least 30% of songs
                                  })
                                  .collect(Collectors.toList());
    }

    // Collaborative Features
    public boolean addCollaborator(String playlistId, User collaborator, User owner) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.getOwner().equals(owner) || collaborator == null) {
            return false;
        }
        
        playlist.addCollaborator(collaborator);
        return true;
    }

    public boolean removeCollaborator(String playlistId, User collaborator, User owner) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.getOwner().equals(owner)) {
            return false;
        }
        
        playlist.removeCollaborator(collaborator);
        return true;
    }

    public List<Playlist> getCollaborativePlaylists(User user) {
        return playlists.values().stream()
                       .filter(playlist -> playlist.getCollaborators().contains(user))
                       .collect(Collectors.toList());
    }

    // Social Features
    public boolean followPlaylist(String playlistId, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.isPublic() || user == null) {
            return false;
        }
        
        playlist.follow(user);
        return true;
    }

    public boolean unfollowPlaylist(String playlistId, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || user == null) {
            return false;
        }
        
        playlist.unfollow(user);
        return true;
    }

    public List<Playlist> getFollowedPlaylists(User user) {
        return playlists.values().stream()
                       .filter(playlist -> playlist.isFollowedBy(user))
                       .collect(Collectors.toList());
    }

    // Playlist Operations
    public boolean shufflePlaylist(String playlistId, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.canEdit(user)) {
            return false;
        }
        
        playlist.shuffle();
        return true;
    }

    public boolean clearPlaylist(String playlistId, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || !playlist.canEdit(user)) {
            return false;
        }
        
        playlist.clear();
        return true;
    }

    public Playlist duplicatePlaylist(String playlistId, User newOwner, String newName) {
        Playlist originalPlaylist = playlists.get(playlistId);
        if (originalPlaylist == null || !originalPlaylist.isPublic()) {
            return null;
        }
        
        Playlist duplicate = createPlaylist(newName != null ? newName : originalPlaylist.getName() + " (Copy)", newOwner);
        
        // Copy all songs
        for (Song song : originalPlaylist.getSongs()) {
            duplicate.addSong(song);
        }
        
        duplicate.setDescription("Copy of " + originalPlaylist.getName());
        return duplicate;
    }

    // Smart Playlist Generation
    public Playlist createSmartPlaylist(String name, User owner, List<Song> seedSongs, int targetSize) {
        Playlist smartPlaylist = createPlaylist(name, owner);
        
        if (seedSongs == null || seedSongs.isEmpty()) {
            return smartPlaylist;
        }
        
        // Add seed songs
        seedSongs.forEach(smartPlaylist::addSong);
        
        // Generate similar songs to fill the playlist
        Set<String> genres = seedSongs.stream()
                                     .map(Song::getGenre)
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toSet());
        
        Set<String> artists = seedSongs.stream()
                                      .map(Song::getArtist)
                                      .filter(Objects::nonNull)
                                      .collect(Collectors.toSet());
        
        // This would typically integrate with MusicLibraryService
        // For now, we'll just mark it as a smart playlist
        smartPlaylist.getMetadata().put("isSmartPlaylist", true);
        smartPlaylist.getMetadata().put("seedGenres", genres);
        smartPlaylist.getMetadata().put("seedArtists", artists);
        
        return smartPlaylist;
    }

    // Analytics and Statistics
    public Map<String, Object> getPlaylistStatistics(String playlistId) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("songCount", playlist.size());
        stats.put("totalDuration", playlist.getFormattedDuration());
        stats.put("totalPlayCount", playlist.getTotalPlayCount());
        stats.put("averageRating", playlist.getAverageRating());
        stats.put("uniqueArtists", playlist.getUniqueArtists().size());
        stats.put("genreDistribution", playlist.getGenreDistribution());
        stats.put("followerCount", playlist.getFollowers().size());
        stats.put("collaboratorCount", playlist.getCollaborators().size());
        stats.put("isPublic", playlist.isPublic());
        stats.put("isCollaborative", playlist.isCollaborative());
        stats.put("createdDate", playlist.getCreatedDate());
        stats.put("lastModified", playlist.getLastModified());
        
        return stats;
    }

    public List<Playlist> getTrendingPlaylists(int limit) {
        return getPublicPlaylists().stream()
                                  .sorted((p1, p2) -> {
                                      // Sort by recent activity and follower growth
                                      int followerDiff = p2.getFollowers().size() - p1.getFollowers().size();
                                      if (followerDiff != 0) return followerDiff;
                                      
                                      // Then by last modified (more recent first)
                                      return p2.getLastModified().compareTo(p1.getLastModified());
                                  })
                                  .limit(limit)
                                  .collect(Collectors.toList());
    }

    // Utility Methods
    public int getTotalPlaylists() {
        return playlists.size();
    }

    public int getTotalPublicPlaylists() {
        return (int) playlists.values().stream().filter(Playlist::isPublic).count();
    }

    public int getTotalCollaborativePlaylists() {
        return (int) playlists.values().stream().filter(Playlist::isCollaborative).count();
    }

    // Private Helper Methods
    private void removeFromIndices(Playlist playlist) {
        // Remove from user index
        List<Playlist> userPlaylists = userPlaylistIndex.get(playlist.getOwner().getId());
        if (userPlaylists != null) {
            userPlaylists.remove(playlist);
            if (userPlaylists.isEmpty()) {
                userPlaylistIndex.remove(playlist.getOwner().getId());
            }
        }
        
        // Remove from public index
        if (playlist.isPublic()) {
            List<Playlist> publicPlaylists = publicPlaylistIndex.get("public");
            if (publicPlaylists != null) {
                publicPlaylists.remove(playlist);
            }
        }
    }

    // Batch Operations
    public boolean addMultipleSongsToPlaylist(String playlistId, List<Song> songs, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || songs == null || !playlist.canEdit(user)) {
            return false;
        }
        
        songs.forEach(playlist::addSong);
        return true;
    }

    public boolean removeMultipleSongsFromPlaylist(String playlistId, List<Song> songs, User user) {
        Playlist playlist = playlists.get(playlistId);
        if (playlist == null || songs == null || !playlist.canEdit(user)) {
            return false;
        }
        
        songs.forEach(playlist::removeSong);
        return true;
    }

    // Additional methods for compatibility

    public boolean updatePlaylist(Playlist playlist) {
        if (playlist == null || playlist.getId() == null) {
            return false;
        }
        playlists.put(playlist.getId(), playlist);
        return true;
    }

    public List<Playlist> getAllPublicPlaylists() {
        return getPublicPlaylists();
    }

    public List<Playlist> getAllPlaylists() {
        return new ArrayList<>(playlists.values());
    }

    public List<Playlist> getUserPlaylists(String userId) {
        if (userId == null) return new ArrayList<>();
        return userPlaylistIndex.getOrDefault(userId, new ArrayList<>());
    }
}
