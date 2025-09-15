package com.musicplayer.player;

import com.musicplayer.model.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Advanced playback controller managing queue, shuffle, repeat modes,
 * and coordinating between different audio players.
 */
public class PlaybackController implements AudioPlayer.PlaybackListener {
    private AudioPlayer audioPlayer;
    private Queue<Song> playQueue;
    private List<Song> originalQueue; // For shuffle/unshuffle
    private List<Song> playHistory;
    private boolean shuffleEnabled;
    private RepeatMode repeatMode;
    private boolean crossfadeEnabled;
    private int crossfadeDurationSeconds;
    private User currentUser;
    private List<PlaybackControllerListener> listeners;

    // Repeat modes
    public enum RepeatMode {
        OFF,        // No repeat
        TRACK,      // Repeat current track
        QUEUE       // Repeat entire queue
    }

    // Listener interface for playback controller events
    public interface PlaybackControllerListener {
        void onQueueChanged(List<Song> newQueue);
        void onShuffleChanged(boolean shuffleEnabled);
        void onRepeatModeChanged(RepeatMode repeatMode);
        void onTrackChanged(Song previousTrack, Song currentTrack);
        void onQueueEnded();
    }

    public PlaybackController(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.playQueue = new LinkedList<>();
        this.originalQueue = new ArrayList<>();
        this.playHistory = new ArrayList<>();
        this.shuffleEnabled = false;
        this.repeatMode = RepeatMode.OFF;
        this.crossfadeEnabled = false;
        this.crossfadeDurationSeconds = 3;
        this.listeners = new ArrayList<>();
        
        // Register as listener to the audio player
        if (this.audioPlayer != null) {
            this.audioPlayer.addPlaybackListener(this);
        }
    }

    // Queue Management
    public void setQueue(List<Song> songs) {
        if (songs == null) {
            clearQueue();
            return;
        }
        
        playQueue.clear();
        originalQueue.clear();
        
        List<Song> availableSongs = songs.stream()
                                        .filter(song -> song != null && song.isDownloaded())
                                        .toList();
        
        originalQueue.addAll(availableSongs);
        
        if (shuffleEnabled) {
            List<Song> shuffledSongs = new ArrayList<>(availableSongs);
            Collections.shuffle(shuffledSongs);
            playQueue.addAll(shuffledSongs);
        } else {
            playQueue.addAll(availableSongs);
        }
        
        notifyQueueChanged();
        System.out.println("🎵 Queue set with " + playQueue.size() + " songs" + 
                          (shuffleEnabled ? " (shuffled)" : ""));
    }

    public void addToQueue(Song song) {
        if (song != null && song.isDownloaded()) {
            playQueue.offer(song);
            originalQueue.add(song);
            notifyQueueChanged();
            System.out.println("🎵 Added to queue: " + song.getDisplayName());
        }
    }

    public void addToQueueNext(Song song) {
        if (song != null && song.isDownloaded()) {
            // Add as the next song to play
            List<Song> queueList = new ArrayList<>(playQueue);
            queueList.add(0, song);
            playQueue.clear();
            playQueue.addAll(queueList);
            
            originalQueue.add(song);
            notifyQueueChanged();
            System.out.println("🎵 Added next in queue: " + song.getDisplayName());
        }
    }

    public void removeFromQueue(Song song) {
        if (playQueue.remove(song)) {
            originalQueue.remove(song);
            notifyQueueChanged();
            System.out.println("🎵 Removed from queue: " + song.getDisplayName());
        }
    }

    public void clearQueue() {
        playQueue.clear();
        originalQueue.clear();
        notifyQueueChanged();
        System.out.println("🎵 Queue cleared");
    }

    public List<Song> getQueue() {
        return new ArrayList<>(playQueue);
    }

    public int getQueueSize() {
        return playQueue.size();
    }

    public boolean isQueueEmpty() {
        return playQueue.isEmpty();
    }

    // Playback Control
    public boolean playNext() {
        if (repeatMode == RepeatMode.TRACK && audioPlayer.getCurrentSong() != null) {
            // Repeat current track
            return audioPlayer.restart();
        }
        
        Song nextSong = getNextSong();
        if (nextSong != null) {
            Song previousSong = audioPlayer.getCurrentSong();
            
            if (crossfadeEnabled && audioPlayer.isPlaying()) {
                // Implement crossfade (simplified)
                if (audioPlayer instanceof LocalAudioPlayer) {
                    ((LocalAudioPlayer) audioPlayer).fadeOut(crossfadeDurationSeconds / 2);
                }
            }
            
            boolean success = audioPlayer.loadSong(nextSong) && audioPlayer.play();
            
            if (success) {
                addToHistory(previousSong);
                notifyTrackChanged(previousSong, nextSong);
                
                if (currentUser != null) {
                    // Add to user's recently played
                    currentUser.addToRecentlyPlayed(nextSong);
                }
            }
            
            return success;
        } else {
            // Queue ended
            handleQueueEnd();
            return false;
        }
    }

    public boolean playPrevious() {
        // If we're more than 3 seconds into the song, restart current song
        if (audioPlayer.getCurrentPositionSeconds() > 3) {
            return audioPlayer.restart();
        }
        
        Song previousSong = getPreviousSong();
        if (previousSong != null) {
            Song currentSong = audioPlayer.getCurrentSong();
            
            boolean success = audioPlayer.loadSong(previousSong) && audioPlayer.play();
            
            if (success) {
                // Add current song back to front of queue
                if (currentSong != null) {
                    List<Song> queueList = new ArrayList<>(playQueue);
                    queueList.add(0, currentSong);
                    playQueue.clear();
                    playQueue.addAll(queueList);
                }
                
                notifyTrackChanged(currentSong, previousSong);
            }
            
            return success;
        }
        
        return false;
    }

    public boolean play() {
        if (audioPlayer.getCurrentSong() != null) {
            return audioPlayer.play();
        } else if (!playQueue.isEmpty()) {
            return playNext();
        }
        return false;
    }

    public boolean pause() {
        return audioPlayer.pause();
    }

    public boolean stop() {
        return audioPlayer.stop();
    }

    public boolean togglePlayPause() {
        return audioPlayer.togglePlayPause();
    }

    // Shuffle and Repeat
    public void setShuffle(boolean enabled) {
        if (this.shuffleEnabled == enabled) return;
        
        this.shuffleEnabled = enabled;
        
        // Rebuild queue with/without shuffle
        List<Song> currentQueue = new ArrayList<>(playQueue);
        Song currentSong = audioPlayer.getCurrentSong();
        
        if (enabled) {
            // Shuffle the remaining queue
            List<Song> remainingQueue = new ArrayList<>(playQueue);
            if (currentSong != null) {
                remainingQueue.remove(currentSong);
            }
            Collections.shuffle(remainingQueue);
            
            playQueue.clear();
            playQueue.addAll(remainingQueue);
        } else {
            // Restore original order
            playQueue.clear();
            List<Song> restoredQueue = new ArrayList<>(originalQueue);
            if (currentSong != null) {
                // Remove songs that have already been played
                int currentIndex = restoredQueue.indexOf(currentSong);
                if (currentIndex >= 0) {
                    restoredQueue = restoredQueue.subList(currentIndex + 1, restoredQueue.size());
                }
            }
            playQueue.addAll(restoredQueue);
        }
        
        notifyShuffleChanged(enabled);
        notifyQueueChanged();
        System.out.println("🔀 Shuffle " + (enabled ? "enabled" : "disabled"));
    }

    public void setRepeatMode(RepeatMode mode) {
        if (this.repeatMode == mode) return;
        
        this.repeatMode = mode;
        notifyRepeatModeChanged(mode);
        System.out.println("🔁 Repeat mode: " + mode);
    }

    public void toggleShuffle() {
        setShuffle(!shuffleEnabled);
    }

    public RepeatMode cycleRepeatMode() {
        RepeatMode nextMode = switch (repeatMode) {
            case OFF -> RepeatMode.TRACK;
            case TRACK -> RepeatMode.QUEUE;
            case QUEUE -> RepeatMode.OFF;
        };
        setRepeatMode(nextMode);
        return nextMode;
    }

    // Crossfade
    public void setCrossfade(boolean enabled, int durationSeconds) {
        this.crossfadeEnabled = enabled;
        this.crossfadeDurationSeconds = Math.max(1, Math.min(12, durationSeconds));
        System.out.println("🎚️ Crossfade " + (enabled ? "enabled" : "disabled") + 
                          " (" + this.crossfadeDurationSeconds + "s)");
    }

    // Playlist Integration
    public void playPlaylist(Playlist playlist) {
        if (playlist != null && !playlist.isEmpty()) {
            setQueue(playlist.getSongs());
            playNext();
            System.out.println("🎵 Playing playlist: " + playlist.getName());
        }
    }

    public void playAlbum(Album album) {
        if (album != null && !album.getTracks().isEmpty()) {
            setQueue(album.getTracks());
            playNext();
            System.out.println("🎵 Playing album: " + album.getDisplayName());
        }
    }

    public void playArtistSongs(Artist artist) {
        if (artist != null && !artist.getSongs().isEmpty()) {
            setQueue(artist.getSongs());
            playNext();
            System.out.println("🎵 Playing artist: " + artist.getName());
        }
    }

    // Private Helper Methods
    private Song getNextSong() {
        return playQueue.poll();
    }

    private Song getPreviousSong() {
        if (!playHistory.isEmpty()) {
            return playHistory.remove(playHistory.size() - 1);
        }
        return null;
    }

    private void addToHistory(Song song) {
        if (song != null) {
            playHistory.add(song);
            // Keep only last 50 songs in history
            if (playHistory.size() > 50) {
                playHistory.remove(0);
            }
        }
    }

    private void handleQueueEnd() {
        if (repeatMode == RepeatMode.QUEUE && !originalQueue.isEmpty()) {
            // Restart the queue
            setQueue(originalQueue);
            playNext();
            System.out.println("🔁 Restarting queue");
        } else {
            // Queue ended
            notifyQueueEnded();
            System.out.println("🎵 Queue ended");
        }
    }

    // AudioPlayer.PlaybackListener implementation
    @Override
    public void onPlaybackStateChanged(AudioPlayer.PlaybackState oldState, AudioPlayer.PlaybackState newState) {
        // Handle automatic track advancement when current track ends
        if (oldState == AudioPlayer.PlaybackState.PLAYING && 
            newState == AudioPlayer.PlaybackState.STOPPED &&
            audioPlayer.getCurrentPositionSeconds() >= audioPlayer.getDurationSeconds() - 1) {
            // Track ended naturally, play next
            playNext();
        }
    }

    @Override
    public void onSongChanged(Song oldSong, Song newSong) {
        // This is handled in playNext/playPrevious methods
    }

    @Override
    public void onPositionChanged(int positionSeconds) {
        // Position updates are handled by the audio player
    }

    @Override
    public void onVolumeChanged(double volume) {
        // Volume changes are handled by the audio player
    }

    @Override
    public void onError(String errorMessage) {
        System.err.println("❌ Playback error: " + errorMessage);
        // Try to play next song on error
        if (!playQueue.isEmpty()) {
            playNext();
        }
    }

    // Listener Management
    public void addListener(PlaybackControllerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(PlaybackControllerListener listener) {
        listeners.remove(listener);
    }

    // Notification Methods
    private void notifyQueueChanged() {
        List<Song> currentQueue = getQueue();
        listeners.forEach(listener -> {
            try {
                listener.onQueueChanged(currentQueue);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyShuffleChanged(boolean enabled) {
        listeners.forEach(listener -> {
            try {
                listener.onShuffleChanged(enabled);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyRepeatModeChanged(RepeatMode mode) {
        listeners.forEach(listener -> {
            try {
                listener.onRepeatModeChanged(mode);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyTrackChanged(Song previousTrack, Song currentTrack) {
        listeners.forEach(listener -> {
            try {
                listener.onTrackChanged(previousTrack, currentTrack);
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    private void notifyQueueEnded() {
        listeners.forEach(listener -> {
            try {
                listener.onQueueEnded();
            } catch (Exception e) {
                System.err.println("❌ Listener error: " + e.getMessage());
            }
        });
    }

    // Getters
    public AudioPlayer getAudioPlayer() { return audioPlayer; }
    public boolean isShuffleEnabled() { return shuffleEnabled; }
    public RepeatMode getRepeatMode() { return repeatMode; }
    public boolean isCrossfadeEnabled() { return crossfadeEnabled; }
    public int getCrossfadeDurationSeconds() { return crossfadeDurationSeconds; }
    public List<Song> getPlayHistory() { return new ArrayList<>(playHistory); }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    // Cleanup
    public void cleanup() {
        if (audioPlayer != null) {
            audioPlayer.removePlaybackListener(this);
            audioPlayer.cleanup();
        }
        clearQueue();
        playHistory.clear();
        listeners.clear();
    }
}
