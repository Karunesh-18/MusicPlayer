package com.musicplayer.player;

import com.musicplayer.model.Song;
import com.musicplayer.model.User;
import java.util.List;

/**
 * Abstract base class for audio players implementing the Strategy pattern.
 * Defines the contract for different audio playback implementations.
 */
public abstract class AudioPlayer {
    protected Song currentSong;
    protected PlaybackState state;
    protected int currentPositionSeconds;
    protected double volume;
    protected List<PlaybackListener> listeners;

    // Playback states
    public enum PlaybackState {
        STOPPED, PLAYING, PAUSED, BUFFERING, ERROR
    }

    // Playback listener interface
    public interface PlaybackListener {
        void onPlaybackStateChanged(PlaybackState oldState, PlaybackState newState);
        void onSongChanged(Song oldSong, Song newSong);
        void onPositionChanged(int positionSeconds);
        void onVolumeChanged(double volume);
        void onError(String errorMessage);
    }

    // Constructor
    public AudioPlayer() {
        this.state = PlaybackState.STOPPED;
        this.currentPositionSeconds = 0;
        this.volume = 0.8;
        this.listeners = new java.util.ArrayList<>();
    }

    // Abstract methods that must be implemented by concrete players
    public abstract boolean play();
    public abstract boolean pause();
    public abstract boolean stop();
    public abstract boolean seek(int positionSeconds);
    public abstract boolean setVolume(double volume);
    public abstract boolean loadSong(Song song);
    public abstract int getDurationSeconds();
    public abstract boolean isSupported();

    // Common functionality
    public Song getCurrentSong() {
        return currentSong;
    }

    public PlaybackState getState() {
        return state;
    }

    public int getCurrentPositionSeconds() {
        return currentPositionSeconds;
    }

    public double getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return state == PlaybackState.PLAYING;
    }

    public boolean isPaused() {
        return state == PlaybackState.PAUSED;
    }

    public boolean isStopped() {
        return state == PlaybackState.STOPPED;
    }

    // Listener management
    public void addPlaybackListener(PlaybackListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removePlaybackListener(PlaybackListener listener) {
        listeners.remove(listener);
    }

    // Protected notification methods
    protected void notifyStateChanged(PlaybackState oldState, PlaybackState newState) {
        this.state = newState;
        listeners.forEach(listener -> {
            try {
                listener.onPlaybackStateChanged(oldState, newState);
            } catch (Exception e) {
                System.err.println("❌ Playback listener error: " + e.getMessage());
            }
        });
    }

    protected void notifySongChanged(Song oldSong, Song newSong) {
        this.currentSong = newSong;
        listeners.forEach(listener -> {
            try {
                listener.onSongChanged(oldSong, newSong);
            } catch (Exception e) {
                System.err.println("❌ Playback listener error: " + e.getMessage());
            }
        });
    }

    protected void notifyPositionChanged(int positionSeconds) {
        this.currentPositionSeconds = positionSeconds;
        listeners.forEach(listener -> {
            try {
                listener.onPositionChanged(positionSeconds);
            } catch (Exception e) {
                System.err.println("❌ Playback listener error: " + e.getMessage());
            }
        });
    }

    protected void notifyVolumeChanged(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        listeners.forEach(listener -> {
            try {
                listener.onVolumeChanged(this.volume);
            } catch (Exception e) {
                System.err.println("❌ Playback listener error: " + e.getMessage());
            }
        });
    }

    protected void notifyError(String errorMessage) {
        PlaybackState oldState = this.state;
        this.state = PlaybackState.ERROR;
        listeners.forEach(listener -> {
            try {
                listener.onError(errorMessage);
                listener.onPlaybackStateChanged(oldState, PlaybackState.ERROR);
            } catch (Exception e) {
                System.err.println("❌ Playback listener error: " + e.getMessage());
            }
        });
    }

    // Utility methods
    public String getFormattedPosition() {
        int minutes = currentPositionSeconds / 60;
        int seconds = currentPositionSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public String getFormattedDuration() {
        int duration = getDurationSeconds();
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public double getProgressPercentage() {
        int duration = getDurationSeconds();
        if (duration <= 0) return 0.0;
        return (double) currentPositionSeconds / duration * 100.0;
    }

    public boolean seekToPercentage(double percentage) {
        int duration = getDurationSeconds();
        if (duration <= 0) return false;
        
        int targetPosition = (int) (duration * percentage / 100.0);
        return seek(targetPosition);
    }

    // Volume control helpers
    public boolean increaseVolume(double increment) {
        return setVolume(volume + increment);
    }

    public boolean decreaseVolume(double decrement) {
        return setVolume(volume - decrement);
    }

    public boolean mute() {
        return setVolume(0.0);
    }

    public boolean setVolumePercentage(int percentage) {
        return setVolume(percentage / 100.0);
    }

    // Playback control helpers
    public boolean togglePlayPause() {
        if (isPlaying()) {
            return pause();
        } else if (isPaused()) {
            return play();
        } else if (currentSong != null) {
            return play();
        }
        return false;
    }

    public boolean restart() {
        return seek(0) && play();
    }

    // Skip functionality (to be used by PlaybackController)
    public boolean skipForward(int seconds) {
        return seek(currentPositionSeconds + seconds);
    }

    public boolean skipBackward(int seconds) {
        return seek(Math.max(0, currentPositionSeconds - seconds));
    }

    // Cleanup
    public void cleanup() {
        stop();
        listeners.clear();
    }

    @Override
    public String toString() {
        return String.format("%s{song='%s', state=%s, position='%s', volume=%.1f%%}", 
                           getClass().getSimpleName(),
                           currentSong != null ? currentSong.getDisplayName() : "None",
                           state,
                           getFormattedPosition(),
                           volume * 100);
    }
}
