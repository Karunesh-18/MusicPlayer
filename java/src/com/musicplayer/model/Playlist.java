package com.musicplayer.model;

import java.time.LocalDateTime;
import java.util.*;

// User playlists - custom song collections
public class Playlist {
    private String id;
    private String name;
    private String description;
    private User owner;
    private List<Song> songs;
    private String coverImageUrl;
    private boolean isPublic;
    private boolean isCollaborative;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    private Set<User> collaborators;
    private Set<User> followers;
    private Map<String, Object> metadata;
    private PlaylistType type;

    // Different kinds of playlists
    public enum PlaylistType {
        USER_CREATED, LIKED_SONGS, RECENTLY_PLAYED, TOP_TRACKS, DISCOVER_WEEKLY, RADIO
    }

    // Constructors
    public Playlist() {
        this.id = generateId();
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.songs = new ArrayList<>();
        this.collaborators = new HashSet<>();
        this.followers = new HashSet<>();
        this.metadata = new HashMap<>();
        this.isPublic = false;
        this.isCollaborative = false;
        this.type = PlaylistType.USER_CREATED;
    }

    public Playlist(String name, User owner) {
        this();
        this.name = name;
        this.owner = owner;
    }

    public Playlist(String name, User owner, boolean isPublic) {
        this(name, owner);
        this.isPublic = isPublic;
    }

    // Core Playlist Operations
    public void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
            updateLastModified();
        }
    }

    public void addSongAtPosition(Song song, int position) {
        if (song != null && position >= 0 && position <= songs.size()) {
            songs.add(position, song);
            updateLastModified();
        }
    }

    public boolean removeSong(Song song) {
        if (songs.remove(song)) {
            updateLastModified();
            return true;
        }
        return false;
    }

    public void removeSongAtPosition(int position) {
        if (position >= 0 && position < songs.size()) {
            songs.remove(position);
            updateLastModified();
        }
    }

    public void moveSong(int fromPosition, int toPosition) {
        if (fromPosition >= 0 && fromPosition < songs.size() && 
            toPosition >= 0 && toPosition < songs.size()) {
            Song song = songs.remove(fromPosition);
            songs.add(toPosition, song);
            updateLastModified();
        }
    }

    public void shuffle() {
        Collections.shuffle(songs);
        updateLastModified();
    }

    public void clear() {
        songs.clear();
        updateLastModified();
    }

    // Advanced Operations
    public List<Song> getShuffledSongs() {
        List<Song> shuffled = new ArrayList<>(songs);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public List<Song> getSongsByArtist(String artistName) {
        return songs.stream()
                   .filter(song -> song.getArtist().equalsIgnoreCase(artistName))
                   .toList();
    }

    public List<Song> getSongsByGenre(String genre) {
        return songs.stream()
                   .filter(song -> genre.equalsIgnoreCase(song.getGenre()))
                   .toList();
    }

    public List<Song> getPopularSongs() {
        return songs.stream()
                   .filter(Song::isPopular)
                   .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                   .toList();
    }

    public List<Song> getRecentlyAdded(int limit) {
        return songs.stream()
                   .sorted((s1, s2) -> s2.getAddedDate().compareTo(s1.getAddedDate()))
                   .limit(limit)
                   .toList();
    }

    // Collaboration Features
    public void addCollaborator(User user) {
        if (user != null && !user.equals(owner)) {
            collaborators.add(user);
            setCollaborative(true);
            updateLastModified();
        }
    }

    public void removeCollaborator(User user) {
        if (collaborators.remove(user)) {
            if (collaborators.isEmpty()) {
                setCollaborative(false);
            }
            updateLastModified();
        }
    }

    public boolean canEdit(User user) {
        return user != null && (user.equals(owner) || 
               (isCollaborative && collaborators.contains(user)));
    }

    // Social Features
    public void follow(User user) {
        if (user != null && isPublic) {
            followers.add(user);
        }
    }

    public void unfollow(User user) {
        followers.remove(user);
    }

    public boolean isFollowedBy(User user) {
        return followers.contains(user);
    }

    // Statistics and Analytics
    public int getTotalDurationSeconds() {
        return songs.stream()
                   .mapToInt(Song::getDurationSeconds)
                   .sum();
    }

    public String getFormattedDuration() {
        int totalSeconds = getTotalDurationSeconds();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    public int getTotalPlayCount() {
        return songs.stream()
                   .mapToInt(Song::getPlayCount)
                   .sum();
    }

    public double getAverageRating() {
        return songs.stream()
                   .mapToDouble(Song::getRating)
                   .filter(rating -> rating > 0)
                   .average()
                   .orElse(0.0);
    }

    public Map<String, Long> getGenreDistribution() {
        return songs.stream()
                   .filter(song -> song.getGenre() != null)
                   .collect(java.util.stream.Collectors.groupingBy(
                       Song::getGenre, 
                       java.util.stream.Collectors.counting()));
    }

    public List<String> getUniqueArtists() {
        return songs.stream()
                   .map(Song::getArtist)
                   .filter(Objects::nonNull)
                   .distinct()
                   .sorted()
                   .toList();
    }

    // Utility Methods
    private String generateId() {
        return "playlist_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    private void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }

    public String getSearchableText() {
        return (name + " " + description + " " + owner.getUsername()).toLowerCase();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    public int size() {
        return songs.size();
    }

    public boolean contains(Song song) {
        return songs.contains(song);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        updateLastModified();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        updateLastModified();
    }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<Song> getSongs() { return new ArrayList<>(songs); }
    public void setSongs(List<Song> songs) { 
        this.songs = new ArrayList<>(songs);
        updateLastModified();
    }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public boolean isCollaborative() { return isCollaborative; }
    public void setCollaborative(boolean collaborative) { this.isCollaborative = collaborative; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public Set<User> getCollaborators() { return new HashSet<>(collaborators); }
    public void setCollaborators(Set<User> collaborators) { this.collaborators = new HashSet<>(collaborators); }

    public Set<User> getFollowers() { return new HashSet<>(followers); }
    public void setFollowers(Set<User> followers) { this.followers = new HashSet<>(followers); }

    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = new HashMap<>(metadata); }

    public PlaylistType getType() { return type; }
    public void setType(PlaylistType type) { this.type = type; }

    // Additional methods for social features
    public String getOwnerId() {
        return owner != null ? owner.getId() : null;
    }

    public void addLike() {
        // Increment like count in metadata
        int currentLikes = getLikeCount();
        metadata.put("likeCount", currentLikes + 1);
    }

    public void removeLike() {
        // Decrement like count in metadata
        int currentLikes = getLikeCount();
        metadata.put("likeCount", Math.max(0, currentLikes - 1));
    }

    public int getLikeCount() {
        Object likes = metadata.get("likeCount");
        return likes instanceof Integer ? (Integer) likes : 0;
    }

    public void addFollower(String userId) {
        // This would typically be handled by the service layer
        // For now, just update follower count in metadata
        int currentFollowers = getFollowerCount();
        metadata.put("followerCount", currentFollowers + 1);
    }

    public void removeFollower(String userId) {
        // This would typically be handled by the service layer
        // For now, just update follower count in metadata
        int currentFollowers = getFollowerCount();
        metadata.put("followerCount", Math.max(0, currentFollowers - 1));
    }

    public int getFollowerCount() {
        Object followers = metadata.get("followerCount");
        return followers instanceof Integer ? (Integer) followers : this.followers.size();
    }

    // Object Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Playlist{id='%s', name='%s', owner='%s', songs=%d, duration='%s', public=%s, collaborative=%s}", 
                           id, name, owner != null ? owner.getUsername() : "Unknown", 
                           songs.size(), getFormattedDuration(), isPublic, isCollaborative);
    }
}
