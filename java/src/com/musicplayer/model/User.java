package com.musicplayer.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user account with comprehensive profile and preference management.
 * Supports social features, subscription management, and personalization.
 */
public class User {
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private String displayName;
    private String profileImageUrl;
    private LocalDateTime createdDate;
    private LocalDateTime lastLoginDate;
    private UserPreferences preferences;
    private SubscriptionType subscription;
    private List<Playlist> playlists;
    private List<Song> likedSongs;
    private Set<Artist> followedArtists;
    private Set<User> following;
    private Set<User> followers;
    private List<Song> recentlyPlayed;
    private Map<String, Object> statistics;
    private boolean isActive;
    private boolean isVerified;

    // Enum for subscription types
    public enum SubscriptionType {
        FREE, PREMIUM, FAMILY, STUDENT
    }

    // Inner class for user preferences
    public static class UserPreferences {
        private boolean shuffleEnabled;
        private RepeatMode repeatMode;
        private double volume;
        private String preferredQuality;
        private boolean crossfadeEnabled;
        private int crossfadeDuration;
        private boolean notificationsEnabled;
        private String theme;
        private String language;

        public enum RepeatMode {
            OFF, TRACK, PLAYLIST
        }

        // Constructor with defaults
        public UserPreferences() {
            this.shuffleEnabled = false;
            this.repeatMode = RepeatMode.OFF;
            this.volume = 0.8;
            this.preferredQuality = "192k";
            this.crossfadeEnabled = false;
            this.crossfadeDuration = 3;
            this.notificationsEnabled = true;
            this.theme = "dark";
            this.language = "en";
        }

        // Getters and Setters
        public boolean isShuffleEnabled() { return shuffleEnabled; }
        public void setShuffleEnabled(boolean shuffleEnabled) { this.shuffleEnabled = shuffleEnabled; }

        public RepeatMode getRepeatMode() { return repeatMode; }
        public void setRepeatMode(RepeatMode repeatMode) { this.repeatMode = repeatMode; }

        public double getVolume() { return volume; }
        public void setVolume(double volume) { 
            this.volume = Math.max(0.0, Math.min(1.0, volume));
        }

        public String getPreferredQuality() { return preferredQuality; }
        public void setPreferredQuality(String preferredQuality) { this.preferredQuality = preferredQuality; }

        public boolean isCrossfadeEnabled() { return crossfadeEnabled; }
        public void setCrossfadeEnabled(boolean crossfadeEnabled) { this.crossfadeEnabled = crossfadeEnabled; }

        public int getCrossfadeDuration() { return crossfadeDuration; }
        public void setCrossfadeDuration(int crossfadeDuration) { 
            this.crossfadeDuration = Math.max(1, Math.min(12, crossfadeDuration));
        }

        public boolean isNotificationsEnabled() { return notificationsEnabled; }
        public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

        public String getTheme() { return theme; }
        public void setTheme(String theme) { this.theme = theme; }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }

    // Constructors
    public User() {
        this.id = generateId();
        this.createdDate = LocalDateTime.now();
        this.preferences = new UserPreferences();
        this.subscription = SubscriptionType.FREE;
        this.playlists = new ArrayList<>();
        this.likedSongs = new ArrayList<>();
        this.followedArtists = new HashSet<>();
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
        this.recentlyPlayed = new ArrayList<>();
        this.statistics = new HashMap<>();
        this.isActive = true;
        this.isVerified = false;
        initializeDefaultPlaylists();
    }

    public User(String username, String email) {
        this();
        this.username = username;
        this.email = email;
        this.displayName = username;
    }

    // Authentication Methods
    public boolean authenticate(String password) {
        // In a real application, use proper password hashing (bcrypt, etc.)
        return this.passwordHash != null && this.passwordHash.equals(hashPassword(password));
    }

    public void setPassword(String password) {
        this.passwordHash = hashPassword(password);
    }

    private String hashPassword(String password) {
        // Simplified hashing - use proper bcrypt in production
        return "hash_" + password.hashCode();
    }

    // Playlist Management
    private void initializeDefaultPlaylists() {
        // Create default playlists
        Playlist likedSongsPlaylist = new Playlist("Liked Songs", this);
        likedSongsPlaylist.setType(Playlist.PlaylistType.LIKED_SONGS);
        playlists.add(likedSongsPlaylist);

        Playlist recentlyPlayedPlaylist = new Playlist("Recently Played", this);
        recentlyPlayedPlaylist.setType(Playlist.PlaylistType.RECENTLY_PLAYED);
        playlists.add(recentlyPlayedPlaylist);
    }

    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name, this);
        playlists.add(playlist);
        return playlist;
    }

    public Playlist createPlaylist(String name, boolean isPublic) {
        Playlist playlist = new Playlist(name, this, isPublic);
        playlists.add(playlist);
        return playlist;
    }

    public boolean deletePlaylist(Playlist playlist) {
        if (playlist != null && playlist.getOwner().equals(this) && 
            playlist.getType() == Playlist.PlaylistType.USER_CREATED) {
            return playlists.remove(playlist);
        }
        return false;
    }

    public Playlist getLikedSongsPlaylist() {
        return playlists.stream()
                       .filter(p -> p.getType() == Playlist.PlaylistType.LIKED_SONGS)
                       .findFirst()
                       .orElse(null);
    }

    // Music Interaction Methods
    public void likeSong(Song song) {
        if (song != null && !likedSongs.contains(song)) {
            likedSongs.add(song);
            song.like();
            
            // Add to liked songs playlist
            Playlist likedPlaylist = getLikedSongsPlaylist();
            if (likedPlaylist != null) {
                likedPlaylist.addSong(song);
            }
        }
    }

    public void unlikeSong(Song song) {
        if (likedSongs.remove(song)) {
            song.unlike();
            
            // Remove from liked songs playlist
            Playlist likedPlaylist = getLikedSongsPlaylist();
            if (likedPlaylist != null) {
                likedPlaylist.removeSong(song);
            }
        }
    }

    public void addToRecentlyPlayed(Song song) {
        if (song != null) {
            recentlyPlayed.remove(song); // Remove if already exists
            recentlyPlayed.add(0, song); // Add to beginning
            
            // Keep only last 50 songs
            if (recentlyPlayed.size() > 50) {
                recentlyPlayed = recentlyPlayed.subList(0, 50);
            }
        }
    }

    // Social Features
    public void followArtist(Artist artist) {
        if (artist != null) {
            followedArtists.add(artist);
            artist.follow();
        }
    }

    public void unfollowArtist(Artist artist) {
        if (followedArtists.remove(artist)) {
            artist.unfollow();
        }
    }

    public void followUser(User user) {
        if (user != null && !user.equals(this)) {
            following.add(user);
            user.addFollower(this);
        }
    }

    public void unfollowUser(User user) {
        if (following.remove(user)) {
            user.removeFollower(this);
        }
    }

    private void addFollower(User user) {
        followers.add(user);
    }

    private void removeFollower(User user) {
        followers.remove(user);
    }

    // Statistics and Analytics
    public int getTotalPlayCount() {
        return likedSongs.stream()
                        .mapToInt(Song::getPlayCount)
                        .sum();
    }

    public Map<String, Long> getTopGenres() {
        return likedSongs.stream()
                        .filter(song -> song.getGenre() != null)
                        .collect(java.util.stream.Collectors.groupingBy(
                            Song::getGenre, 
                            java.util.stream.Collectors.counting()));
    }

    public List<Artist> getTopArtists(int limit) {
        return likedSongs.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                            song -> song.getArtist(),
                            java.util.stream.Collectors.counting()))
                        .entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(limit)
                        .map(Map.Entry::getKey)
                        .map(artistName -> followedArtists.stream()
                                                        .filter(a -> a.getName().equals(artistName))
                                                        .findFirst()
                                                        .orElse(new Artist(artistName)))
                        .toList();
    }

    // Utility Methods
    private String generateId() {
        return "user_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public void updateLastLogin() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public boolean isPremium() {
        return subscription == SubscriptionType.PREMIUM || 
               subscription == SubscriptionType.FAMILY;
    }

    public String getSearchableText() {
        return (username + " " + displayName + " " + email).toLowerCase();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(LocalDateTime lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }

    public SubscriptionType getSubscription() { return subscription; }
    public void setSubscription(SubscriptionType subscription) { this.subscription = subscription; }

    public List<Playlist> getPlaylists() { return new ArrayList<>(playlists); }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = new ArrayList<>(playlists); }

    public List<Song> getLikedSongs() { return new ArrayList<>(likedSongs); }
    public void setLikedSongs(List<Song> likedSongs) { this.likedSongs = new ArrayList<>(likedSongs); }

    public Set<Artist> getFollowedArtists() { return new HashSet<>(followedArtists); }
    public void setFollowedArtists(Set<Artist> followedArtists) { this.followedArtists = new HashSet<>(followedArtists); }

    public Set<User> getFollowing() { return new HashSet<>(following); }
    public void setFollowing(Set<User> following) { this.following = new HashSet<>(following); }

    public Set<User> getFollowers() { return new HashSet<>(followers); }
    public void setFollowers(Set<User> followers) { this.followers = new HashSet<>(followers); }

    public List<Song> getRecentlyPlayed() { return new ArrayList<>(recentlyPlayed); }
    public void setRecentlyPlayed(List<Song> recentlyPlayed) { this.recentlyPlayed = new ArrayList<>(recentlyPlayed); }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    // Object Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', email='%s', subscription=%s, playlists=%d, likedSongs=%d, following=%d, followers=%d}", 
                           id, username, email, subscription, playlists.size(), likedSongs.size(), following.size(), followers.size());
    }
}
