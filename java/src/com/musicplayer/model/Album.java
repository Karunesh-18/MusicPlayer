package com.musicplayer.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a music album with track listing and metadata.
 * Supports album-based operations and statistics.
 */
public class Album {
    private String id;
    private String title;
    private Artist artist;
    private List<Song> tracks;
    private String coverArtUrl;
    private LocalDateTime releaseDate;
    private LocalDateTime addedDate;
    private String genre;
    private String recordLabel;
    private AlbumType type; // ALBUM, EP, SINGLE, COMPILATION
    private int totalDurationSeconds;
    private boolean isLiked;
    private double rating;

    // Enum for album types
    public enum AlbumType {
        ALBUM, EP, SINGLE, COMPILATION, MIXTAPE
    }

    // Constructors
    public Album() {
        this.id = generateId();
        this.addedDate = LocalDateTime.now();
        this.tracks = new ArrayList<>();
        this.type = AlbumType.ALBUM;
        this.isLiked = false;
        this.rating = 0.0;
    }

    public Album(String title, Artist artist) {
        this();
        this.title = title;
        this.artist = artist;
    }

    public Album(String title, Artist artist, AlbumType type) {
        this(title, artist);
        this.type = type;
    }

    // Business Logic Methods
    public void addTrack(Song song) {
        if (song != null && !tracks.contains(song)) {
            tracks.add(song);
            song.setAlbum(this.title);
            song.setArtist(this.artist.getName());
            updateTotalDuration();
        }
    }

    public void removeTrack(Song song) {
        if (tracks.remove(song)) {
            updateTotalDuration();
        }
    }

    public void addTrackAtPosition(Song song, int position) {
        if (song != null && position >= 0 && position <= tracks.size()) {
            tracks.add(position, song);
            song.setAlbum(this.title);
            song.setArtist(this.artist.getName());
            updateTotalDuration();
        }
    }

    public void reorderTracks(List<Song> newOrder) {
        if (newOrder != null && newOrder.size() == tracks.size() && 
            new HashSet<>(newOrder).equals(new HashSet<>(tracks))) {
            this.tracks = new ArrayList<>(newOrder);
        }
    }

    public Song getTrackByNumber(int trackNumber) {
        if (trackNumber > 0 && trackNumber <= tracks.size()) {
            return tracks.get(trackNumber - 1);
        }
        return null;
    }

    public int getTrackNumber(Song song) {
        int index = tracks.indexOf(song);
        return index >= 0 ? index + 1 : -1;
    }

    public List<Song> getPopularTracks() {
        return tracks.stream()
                    .filter(Song::isPopular)
                    .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                    .toList();
    }

    public int getTotalPlayCount() {
        return tracks.stream()
                    .mapToInt(Song::getPlayCount)
                    .sum();
    }

    public double getAverageRating() {
        return tracks.stream()
                    .mapToDouble(Song::getRating)
                    .filter(rating -> rating > 0)
                    .average()
                    .orElse(0.0);
    }

    public String getFormattedDuration() {
        int hours = totalDurationSeconds / 3600;
        int minutes = (totalDurationSeconds % 3600) / 60;
        int seconds = totalDurationSeconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    public void like() {
        this.isLiked = true;
        // Also like all tracks in the album
        tracks.forEach(Song::like);
    }

    public void unlike() {
        this.isLiked = false;
    }

    public void setRating(double rating) {
        if (rating >= 0.0 && rating <= 5.0) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }
    }

    private void updateTotalDuration() {
        this.totalDurationSeconds = tracks.stream()
                                         .mapToInt(Song::getDurationSeconds)
                                         .sum();
    }

    public boolean isComplete() {
        return !tracks.isEmpty() && tracks.stream().allMatch(Song::isDownloaded);
    }

    public int getDownloadedTrackCount() {
        return (int) tracks.stream().filter(Song::isDownloaded).count();
    }

    // Utility Methods
    private String generateId() {
        return "album_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getSearchableText() {
        return (title + " " + artist.getName() + " " + genre + " " + recordLabel).toLowerCase();
    }

    public String getDisplayName() {
        return String.format("%s - %s", artist.getName(), title);
    }

    public boolean isPopular() {
        return getTotalPlayCount() > 50 || rating > 4.0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }

    public List<Song> getTracks() { return new ArrayList<>(tracks); }
    public void setTracks(List<Song> tracks) { 
        this.tracks = new ArrayList<>(tracks);
        updateTotalDuration();
    }

    public String getCoverArtUrl() { return coverArtUrl; }
    public void setCoverArtUrl(String coverArtUrl) { this.coverArtUrl = coverArtUrl; }

    public LocalDateTime getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getRecordLabel() { return recordLabel; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }

    public AlbumType getType() { return type; }
    public void setType(AlbumType type) { this.type = type; }

    public int getTotalDurationSeconds() { return totalDurationSeconds; }

    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }

    public double getRating() { return rating; }

    // Object Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Album{id='%s', title='%s', artist='%s', tracks=%d, duration='%s', type=%s}", 
                           id, title, artist != null ? artist.getName() : "Unknown", 
                           tracks.size(), getFormattedDuration(), type);
    }
}
