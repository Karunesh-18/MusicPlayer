package com.musicplayer.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a music track with comprehensive metadata and functionality.
 * Core model class following OOP principles with proper encapsulation.
 */
public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private int durationSeconds;
    private String genre;
    private String filePath;
    private String albumArtUrl;
    private LocalDateTime releaseDate;
    private LocalDateTime addedDate;
    private int playCount;
    private double rating;
    private boolean isLiked;
    private boolean isDownloaded;
    private long fileSize;
    private String quality; // e.g., "192k", "320k"

    // Constructors
    public Song() {
        this.id = generateId();
        this.addedDate = LocalDateTime.now();
        this.playCount = 0;
        this.rating = 0.0;
        this.isLiked = false;
        this.isDownloaded = false;
    }

    public Song(String title, String artist) {
        this();
        this.title = title;
        this.artist = artist;
    }

    public Song(String title, String artist, String album, int durationSeconds) {
        this(title, artist);
        this.album = album;
        this.durationSeconds = durationSeconds;
    }

    // Business Logic Methods
    public void play() {
        this.playCount++;
    }

    public void like() {
        this.isLiked = true;
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

    public String getFormattedDuration() {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public String getDisplayName() {
        return String.format("%s - %s", artist, title);
    }

    public boolean isPopular() {
        return playCount > 10 || rating > 4.0;
    }

    // Utility Methods
    private String generateId() {
        return "song_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public String getSearchableText() {
        return (title + " " + artist + " " + album + " " + genre).toLowerCase();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { 
        this.filePath = filePath;
        this.isDownloaded = (filePath != null && !filePath.isEmpty());
    }

    public String getAlbumArtUrl() { return albumArtUrl; }
    public void setAlbumArtUrl(String albumArtUrl) { this.albumArtUrl = albumArtUrl; }

    public LocalDateTime getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public double getRating() { return rating; }

    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }

    public boolean isDownloaded() { return isDownloaded; }
    public void setDownloaded(boolean downloaded) { isDownloaded = downloaded; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    // Object Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Song{id='%s', title='%s', artist='%s', album='%s', duration='%s', playCount=%d, liked=%s}", 
                           id, title, artist, album, getFormattedDuration(), playCount, isLiked);
    }
}
