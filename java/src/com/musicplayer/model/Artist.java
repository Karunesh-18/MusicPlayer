package com.musicplayer.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a music artist with comprehensive information and relationships.
 * Supports artist following, song collections, and social features.
 */
public class Artist {
    private String id;
    private String name;
    private String bio;
    private String imageUrl;
    private String genre;
    private int followers;
    private LocalDateTime createdDate;
    private List<Song> songs;
    private List<Album> albums;
    private Map<String, Object> socialLinks; // Spotify, YouTube, etc.
    private boolean isVerified;
    private double popularity; // 0.0 to 100.0

    // Constructors
    public Artist() {
        this.id = generateId();
        this.createdDate = LocalDateTime.now();
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.socialLinks = new HashMap<>();
        this.followers = 0;
        this.popularity = 0.0;
        this.isVerified = false;
    }

    public Artist(String name) {
        this();
        this.name = name;
    }

    public Artist(String name, String genre) {
        this(name);
        this.genre = genre;
    }

    // Business Logic Methods
    public void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
            updatePopularity();
        }
    }

    public void removeSong(Song song) {
        if (songs.remove(song)) {
            updatePopularity();
        }
    }

    public void addAlbum(Album album) {
        if (album != null && !albums.contains(album)) {
            albums.add(album);
            // Add all album songs to artist's song collection
            for (Song song : album.getTracks()) {
                addSong(song);
            }
        }
    }

    public void follow() {
        this.followers++;
    }

    public void unfollow() {
        if (this.followers > 0) {
            this.followers--;
        }
    }

    public List<Song> getTopSongs(int limit) {
        return songs.stream()
                   .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                   .limit(limit)
                   .toList();
    }

    public List<Song> getPopularSongs() {
        return songs.stream()
                   .filter(Song::isPopular)
                   .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                   .toList();
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

    public List<String> getGenres() {
        return songs.stream()
                   .map(Song::getGenre)
                   .filter(Objects::nonNull)
                   .distinct()
                   .toList();
    }

    private void updatePopularity() {
        // Calculate popularity based on total plays, followers, and ratings
        int totalPlays = getTotalPlayCount();
        double avgRating = getAverageRating();
        this.popularity = Math.min(100.0, 
            (totalPlays * 0.1) + (followers * 0.5) + (avgRating * 10));
    }

    // Social Media Methods
    public void addSocialLink(String platform, String url) {
        socialLinks.put(platform, url);
    }

    public String getSocialLink(String platform) {
        return (String) socialLinks.get(platform);
    }

    // Utility Methods
    private String generateId() {
        return "artist_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getSearchableText() {
        return (name + " " + bio + " " + genre).toLowerCase();
    }

    public boolean isPopular() {
        return popularity > 50.0 || followers > 100;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public List<Song> getSongs() { return new ArrayList<>(songs); }
    public void setSongs(List<Song> songs) { 
        this.songs = new ArrayList<>(songs);
        updatePopularity();
    }

    public List<Album> getAlbums() { return new ArrayList<>(albums); }
    public void setAlbums(List<Album> albums) { this.albums = new ArrayList<>(albums); }

    public Map<String, Object> getSocialLinks() { return new HashMap<>(socialLinks); }
    public void setSocialLinks(Map<String, Object> socialLinks) { this.socialLinks = new HashMap<>(socialLinks); }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }

    // Object Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Artist{id='%s', name='%s', genre='%s', songs=%d, albums=%d, followers=%d, popularity=%.1f}", 
                           id, name, genre, songs.size(), albums.size(), followers, popularity);
    }
}
