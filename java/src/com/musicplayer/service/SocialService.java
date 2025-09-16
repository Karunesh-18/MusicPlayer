package com.musicplayer.service;

import com.musicplayer.model.*;
import com.musicplayer.repository.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing social features including user following,
 * playlist sharing, and social interactions.
 */
public class SocialService {
    
    private final UserRepository userRepository;
    private final PlaylistService playlistService;
    private final List<SocialEventListener> listeners;
    
    // Social event types
    public enum SocialEventType {
        USER_FOLLOWED,
        USER_UNFOLLOWED,
        PLAYLIST_SHARED,
        PLAYLIST_LIKED,
        PLAYLIST_COMMENTED,
        USER_ACTIVITY
    }
    
    // Social event listener interface
    public interface SocialEventListener {
        void onSocialEvent(SocialEventType eventType, String userId, String targetId, Map<String, Object> data);
    }
    
    // Social activity types
    public static class SocialActivity {
        private final String id;
        private final String userId;
        private final SocialEventType type;
        private final String targetId;
        private final String description;
        private final Date timestamp;
        private final Map<String, Object> metadata;
        
        public SocialActivity(String userId, SocialEventType type, String targetId, String description) {
            this.id = UUID.randomUUID().toString();
            this.userId = userId;
            this.type = type;
            this.targetId = targetId;
            this.description = description;
            this.timestamp = new Date();
            this.metadata = new HashMap<>();
        }
        
        // Getters
        public String getId() { return id; }
        public String getUserId() { return userId; }
        public SocialEventType getType() { return type; }
        public String getTargetId() { return targetId; }
        public String getDescription() { return description; }
        public Date getTimestamp() { return timestamp; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    public SocialService(UserRepository userRepository, PlaylistService playlistService) {
        this.userRepository = userRepository;
        this.playlistService = playlistService;
        this.listeners = new ArrayList<>();
    }
    
    // User Following Features
    
    /**
     * Follow a user.
     */
    public boolean followUser(String followerId, String followeeId) {
        if (followerId == null || followeeId == null || followerId.equals(followeeId)) {
            return false;
        }
        
        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followeeOpt = userRepository.findById(followeeId);
        
        if (followerOpt.isEmpty() || followeeOpt.isEmpty()) {
            return false;
        }
        
        User follower = followerOpt.get();
        User followee = followeeOpt.get();
        
        // Check if already following
        if (follower.getFollowing().contains(followeeId)) {
            return false; // Already following
        }
        
        // Add to following list
        follower.followUser(followeeId);
        followee.addFollower();
        
        // Save changes
        userRepository.save(follower);
        userRepository.save(followee);
        
        // Create social activity
        SocialActivity activity = new SocialActivity(
            followerId, 
            SocialEventType.USER_FOLLOWED, 
            followeeId,
            follower.getDisplayName() + " started following " + followee.getDisplayName()
        );
        
        // Notify listeners
        notifyListeners(SocialEventType.USER_FOLLOWED, followerId, followeeId, 
                       Map.of("followerName", follower.getDisplayName(), 
                             "followeeName", followee.getDisplayName()));
        
        System.out.println("👥 " + follower.getDisplayName() + " is now following " + followee.getDisplayName());
        return true;
    }
    
    /**
     * Unfollow a user.
     */
    public boolean unfollowUser(String followerId, String followeeId) {
        if (followerId == null || followeeId == null || followerId.equals(followeeId)) {
            return false;
        }
        
        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followeeOpt = userRepository.findById(followeeId);
        
        if (followerOpt.isEmpty() || followeeOpt.isEmpty()) {
            return false;
        }
        
        User follower = followerOpt.get();
        User followee = followeeOpt.get();
        
        // Check if currently following
        if (!follower.getFollowing().contains(followeeId)) {
            return false; // Not following
        }
        
        // Remove from following list
        follower.unfollowUser(followeeId);
        followee.removeFollower();
        
        // Save changes
        userRepository.save(follower);
        userRepository.save(followee);
        
        // Notify listeners
        notifyListeners(SocialEventType.USER_UNFOLLOWED, followerId, followeeId,
                       Map.of("followerName", follower.getDisplayName(),
                             "followeeName", followee.getDisplayName()));
        
        System.out.println("👥 " + follower.getDisplayName() + " unfollowed " + followee.getDisplayName());
        return true;
    }
    
    /**
     * Check if a user is following another user.
     */
    public boolean isFollowing(String followerId, String followeeId) {
        if (followerId == null || followeeId == null || followerId.equals(followeeId)) {
            return false;
        }
        
        Optional<User> followerOpt = userRepository.findById(followerId);
        return followerOpt.map(user -> user.getFollowing().contains(followeeId)).orElse(false);
    }
    
    /**
     * Get users that a user is following.
     */
    public List<User> getFollowing(String userId) {
        if (userId == null) {
            return List.of();
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        
        List<String> followingIds = userOpt.get().getFollowing();
        return userRepository.findAllById(followingIds);
    }
    
    /**
     * Get users that follow a user.
     */
    public List<User> getFollowers(String userId) {
        if (userId == null) {
            return List.of();
        }
        
        return userRepository.findFollowersOf(userId);
    }
    
    /**
     * Get mutual followers between two users.
     */
    public List<User> getMutualFollowers(String userId1, String userId2) {
        if (userId1 == null || userId2 == null || userId1.equals(userId2)) {
            return List.of();
        }
        
        if (userRepository instanceof JsonUserRepository) {
            return ((JsonUserRepository) userRepository).findMutualFollowers(userId1, userId2);
        }
        
        // Fallback implementation
        List<User> followers1 = getFollowers(userId1);
        List<User> followers2 = getFollowers(userId2);
        
        return followers1.stream()
                        .filter(followers2::contains)
                        .collect(Collectors.toList());
    }
    
    /**
     * Suggest users to follow based on mutual connections.
     */
    public List<User> suggestUsersToFollow(String userId, int limit) {
        if (userId == null) {
            return List.of();
        }
        
        if (userRepository instanceof JsonUserRepository) {
            return ((JsonUserRepository) userRepository).suggestFollowing(userId, limit);
        }
        
        // Fallback implementation
        List<User> following = getFollowing(userId);
        Set<User> suggestions = new HashSet<>();
        
        // Get users followed by people the user follows
        for (User followedUser : following) {
            List<User> theirFollowing = getFollowing(followedUser.getId());
            suggestions.addAll(theirFollowing);
        }
        
        // Remove the user themselves and people they already follow
        suggestions.removeIf(user -> user.getId().equals(userId) || 
                           following.stream().anyMatch(f -> f.getId().equals(user.getId())));
        
        return suggestions.stream()
                         .sorted(Comparator.comparingInt(User::getFollowerCount).reversed())
                         .limit(Math.max(0, limit))
                         .collect(Collectors.toList());
    }
    
    // Playlist Sharing Features
    
    /**
     * Share a playlist publicly.
     */
    public boolean sharePlaylist(String playlistId, String userId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistByIdOptional(playlistId);
        if (playlistOpt.isEmpty()) {
            return false;
        }
        
        Playlist playlist = playlistOpt.get();
        
        // Check if user owns the playlist
        if (!playlist.getOwnerId().equals(userId)) {
            return false;
        }
        
        // Make playlist public
        playlist.setPublic(true);
        playlistService.updatePlaylist(playlist);
        
        // Create social activity
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Notify listeners
            notifyListeners(SocialEventType.PLAYLIST_SHARED, userId, playlistId,
                           Map.of("playlistName", playlist.getName(),
                                 "userName", user.getDisplayName()));
            
            System.out.println("📋 " + user.getDisplayName() + " shared playlist: " + playlist.getName());
        }
        
        return true;
    }
    
    /**
     * Like a playlist.
     */
    public boolean likePlaylist(String playlistId, String userId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistByIdOptional(playlistId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (playlistOpt.isEmpty() || userOpt.isEmpty()) {
            return false;
        }
        
        Playlist playlist = playlistOpt.get();
        User user = userOpt.get();
        
        // Add to user's liked playlists
        if (user.getLikedPlaylists().contains(playlistId)) {
            return false; // Already liked
        }
        
        user.getLikedPlaylists().add(playlistId);
        playlist.addLike();
        
        // Save changes
        userRepository.save(user);
        playlistService.updatePlaylist(playlist);
        
        // Notify listeners
        notifyListeners(SocialEventType.PLAYLIST_LIKED, userId, playlistId,
                       Map.of("playlistName", playlist.getName(),
                             "userName", user.getDisplayName()));
        
        System.out.println("❤️ " + user.getDisplayName() + " liked playlist: " + playlist.getName());
        return true;
    }
    
    /**
     * Unlike a playlist.
     */
    public boolean unlikePlaylist(String playlistId, String userId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistByIdOptional(playlistId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (playlistOpt.isEmpty() || userOpt.isEmpty()) {
            return false;
        }
        
        Playlist playlist = playlistOpt.get();
        User user = userOpt.get();
        
        // Remove from user's liked playlists
        if (!user.getLikedPlaylists().contains(playlistId)) {
            return false; // Not liked
        }
        
        user.getLikedPlaylists().remove(playlistId);
        playlist.removeLike();
        
        // Save changes
        userRepository.save(user);
        playlistService.updatePlaylist(playlist);
        
        System.out.println("💔 " + user.getDisplayName() + " unliked playlist: " + playlist.getName());
        return true;
    }
    
    /**
     * Follow a playlist (get updates when it changes).
     */
    public boolean followPlaylist(String playlistId, String userId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistByIdOptional(playlistId);
        if (playlistOpt.isEmpty()) {
            return false;
        }
        
        Playlist playlist = playlistOpt.get();
        
        // Check if already following
        if (playlist.getFollowers().contains(userId)) {
            return false;
        }
        
        playlist.addFollower(userId);
        playlistService.updatePlaylist(playlist);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("👁️ " + user.getDisplayName() + " is now following playlist: " + playlist.getName());
        }
        
        return true;
    }
    
    /**
     * Unfollow a playlist.
     */
    public boolean unfollowPlaylist(String playlistId, String userId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistByIdOptional(playlistId);
        if (playlistOpt.isEmpty()) {
            return false;
        }
        
        Playlist playlist = playlistOpt.get();
        
        // Check if currently following
        if (!playlist.getFollowers().contains(userId)) {
            return false;
        }
        
        playlist.removeFollower(userId);
        playlistService.updatePlaylist(playlist);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("👁️ " + user.getDisplayName() + " unfollowed playlist: " + playlist.getName());
        }
        
        return true;
    }
    
    // Discovery Features
    
    /**
     * Get popular playlists (most liked/followed).
     */
    public List<Playlist> getPopularPlaylists(int limit) {
        return playlistService.getAllPublicPlaylists().stream()
                             .sorted((p1, p2) -> {
                                 int score1 = p1.getLikeCount() + p1.getFollowers().size();
                                 int score2 = p2.getLikeCount() + p2.getFollowers().size();
                                 return Integer.compare(score2, score1);
                             })
                             .limit(Math.max(0, limit))
                             .collect(Collectors.toList());
    }
    
    /**
     * Get trending playlists (recently popular).
     */
    public List<Playlist> getTrendingPlaylists(int limit) {
        // For simplicity, return recently created public playlists with activity
        return playlistService.getAllPublicPlaylists().stream()
                             .filter(p -> p.getLikeCount() > 0 || !p.getFollowers().isEmpty())
                             .sorted(Comparator.comparing(Playlist::getCreatedDate).reversed())
                             .limit(Math.max(0, limit))
                             .collect(Collectors.toList());
    }
    
    /**
     * Get playlists recommended for a user based on their activity.
     */
    public List<Playlist> getRecommendedPlaylists(String userId, int limit) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        
        User user = userOpt.get();
        
        // Get playlists from users they follow
        List<User> following = getFollowing(userId);
        List<Playlist> recommendedPlaylists = new ArrayList<>();
        
        for (User followedUser : following) {
            List<Playlist> theirPlaylists = playlistService.getUserPlaylists(followedUser.getId());
            recommendedPlaylists.addAll(theirPlaylists.stream()
                                                    .filter(Playlist::isPublic)
                                                    .collect(Collectors.toList()));
        }
        
        // Remove duplicates and playlists user already likes
        return recommendedPlaylists.stream()
                                  .distinct()
                                  .filter(p -> !user.getLikedPlaylists().contains(p.getId()))
                                  .filter(p -> !p.getOwnerId().equals(userId))
                                  .sorted((p1, p2) -> {
                                      int score1 = p1.getLikeCount() + p1.getFollowers().size();
                                      int score2 = p2.getLikeCount() + p2.getFollowers().size();
                                      return Integer.compare(score2, score1);
                                  })
                                  .limit(Math.max(0, limit))
                                  .collect(Collectors.toList());
    }
    
    // Event Management
    
    /**
     * Add a social event listener.
     */
    public void addSocialEventListener(SocialEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Remove a social event listener.
     */
    public void removeSocialEventListener(SocialEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(SocialEventType eventType, String userId, String targetId, Map<String, Object> data) {
        listeners.forEach(listener -> {
            try {
                listener.onSocialEvent(eventType, userId, targetId, data);
            } catch (Exception e) {
                System.err.println("❌ Social event listener error: " + e.getMessage());
            }
        });
    }
    
    // Statistics
    
    /**
     * Get social statistics for a user.
     */
    public Map<String, Object> getUserSocialStats(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Map.of();
        }
        
        User user = userOpt.get();
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("followerCount", user.getFollowerCount());
        stats.put("followingCount", user.getFollowing().size());
        stats.put("likedPlaylistsCount", user.getLikedPlaylists().size());
        stats.put("playlistsCreated", playlistService.getUserPlaylists(userId).size());
        stats.put("publicPlaylistsCreated", playlistService.getUserPlaylists(userId).stream()
                                                          .mapToInt(p -> p.isPublic() ? 1 : 0)
                                                          .sum());
        
        return stats;
    }
    
    /**
     * Get overall social platform statistics.
     */
    public Map<String, Object> getPlatformSocialStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        List<Playlist> allPlaylists = playlistService.getAllPlaylists();
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalPlaylists", allPlaylists.size());
        stats.put("publicPlaylists", allPlaylists.stream().mapToInt(p -> p.isPublic() ? 1 : 0).sum());
        stats.put("totalPlaylistLikes", allPlaylists.stream().mapToInt(Playlist::getLikeCount).sum());
        
        if (userRepository instanceof JsonUserRepository) {
            JsonUserRepository jsonRepo = (JsonUserRepository) userRepository;
            stats.put("totalFollowConnections", jsonRepo.getTotalFollowingCount());
            stats.put("averageFollowerCount", jsonRepo.getAverageFollowerCount());
        }
        
        return stats;
    }
}
