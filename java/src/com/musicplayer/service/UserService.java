package com.musicplayer.service;

import com.musicplayer.model.*;
import java.util.*;
import java.util.stream.Collectors;

// Handles user accounts, login, registration etc
public class UserService {
    private Map<String, User> users;
    private Map<String, User> usernameIndex;
    private Map<String, User> emailIndex;
    private User currentUser;

    public UserService() {
        this.users = new HashMap<>();
        this.usernameIndex = new HashMap<>();
        this.emailIndex = new HashMap<>();
    }

    // Creating accounts and logging in
    public User registerUser(String username, String email, String displayName, String password) {
        User user = registerUser(username, email, password);
        if (displayName != null && !displayName.trim().isEmpty()) {
            user.setDisplayName(displayName.trim());
        }
        return user;
    }

    public User registerUser(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("You need to provide a username");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password needs to be at least 6 characters");
        }

        // Make sure username and email aren't taken
        if (usernameIndex.containsKey(username.toLowerCase())) {
            throw new IllegalArgumentException("That username is already taken");
        }
        if (emailIndex.containsKey(email.toLowerCase())) {
            throw new IllegalArgumentException("Someone already used that email");
        }

        User user = new User(username.trim(), email.trim());
        user.setPassword(password);
        
        // Add to storage and indices
        users.put(user.getId(), user);
        usernameIndex.put(username.toLowerCase(), user);
        emailIndex.put(email.toLowerCase(), user);
        
        return user;
    }

    public Optional<User> authenticateUser(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || password == null) {
            return Optional.empty();
        }

        User user = getUserByUsername(usernameOrEmail);
        if (user == null) {
            user = getUserByEmail(usernameOrEmail);
        }

        if (user != null && user.authenticate(password)) {
            user.updateLastLogin();
            this.currentUser = user;
            return Optional.of(user);
        }

        return Optional.empty();
    }

    public void logoutUser() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    // User Retrieval
    public User getUserById(String id) {
        return users.get(id);
    }

    public User getUserByUsername(String username) {
        if (username == null) return null;
        return usernameIndex.get(username.toLowerCase());
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;
        return emailIndex.get(email.toLowerCase());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // User Profile Management
    public boolean updateUserProfile(String userId, String displayName, String profileImageUrl) {
        User user = users.get(userId);
        if (user == null) return false;

        if (displayName != null && !displayName.trim().isEmpty()) {
            user.setDisplayName(displayName.trim());
        }
        if (profileImageUrl != null) {
            user.setProfileImageUrl(profileImageUrl);
        }

        return true;
    }

    public boolean updateUserPreferences(String userId, User.UserPreferences preferences) {
        User user = users.get(userId);
        if (user == null || preferences == null) return false;

        user.setPreferences(preferences);
        return true;
    }

    public boolean updateUserSubscription(String userId, User.SubscriptionType subscription) {
        User user = users.get(userId);
        if (user == null || subscription == null) return false;

        user.setSubscription(subscription);
        return true;
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = users.get(userId);
        if (user == null || !user.authenticate(oldPassword)) {
            return false;
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password should be at least 6 characters");
        }

        user.setPassword(newPassword);
        return true;
    }

    // Social Features
    public boolean followUser(String followerId, String followeeId) {
        User follower = users.get(followerId);
        User followee = users.get(followeeId);
        
        if (follower == null || followee == null || follower.equals(followee)) {
            return false;
        }

        follower.followUser(followee);
        return true;
    }

    public boolean unfollowUser(String followerId, String followeeId) {
        User follower = users.get(followerId);
        User followee = users.get(followeeId);
        
        if (follower == null || followee == null) {
            return false;
        }

        follower.unfollowUser(followee);
        return true;
    }

    public boolean followArtist(String userId, Artist artist) {
        User user = users.get(userId);
        if (user == null || artist == null) return false;

        user.followArtist(artist);
        return true;
    }

    public boolean unfollowArtist(String userId, Artist artist) {
        User user = users.get(userId);
        if (user == null || artist == null) return false;

        user.unfollowArtist(artist);
        return true;
    }

    public List<User> getFollowers(String userId) {
        User user = users.get(userId);
        return user != null ? new ArrayList<>(user.getFollowers()) : new ArrayList<>();
    }

    public List<User> getFollowing(String userId) {
        User user = users.get(userId);
        return user != null ? new ArrayList<>(user.getFollowing()) : new ArrayList<>();
    }

    // Music Interaction
    public boolean likeSong(String userId, Song song) {
        User user = users.get(userId);
        if (user == null || song == null) return false;

        user.likeSong(song);
        return true;
    }

    public boolean unlikeSong(String userId, Song song) {
        User user = users.get(userId);
        if (user == null || song == null) return false;

        user.unlikeSong(song);
        return true;
    }

    public boolean addToRecentlyPlayed(String userId, Song song) {
        User user = users.get(userId);
        if (user == null || song == null) return false;

        user.addToRecentlyPlayed(song);
        return true;
    }

    public List<Song> getLikedSongs(String userId) {
        User user = users.get(userId);
        return user != null ? user.getLikedSongs() : new ArrayList<>();
    }

    public List<Song> getRecentlyPlayed(String userId) {
        User user = users.get(userId);
        return user != null ? user.getRecentlyPlayed() : new ArrayList<>();
    }

    // User Search and Discovery
    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase();
        return users.values().stream()
                   .filter(user -> user.getSearchableText().contains(lowerQuery))
                   .filter(User::isActive)
                   .sorted((u1, u2) -> {
                       // Prioritize exact username matches
                       boolean u1UsernameMatch = u1.getUsername().toLowerCase().contains(lowerQuery);
                       boolean u2UsernameMatch = u2.getUsername().toLowerCase().contains(lowerQuery);
                       if (u1UsernameMatch && !u2UsernameMatch) return -1;
                       if (!u1UsernameMatch && u2UsernameMatch) return 1;
                       
                       // Then by follower count
                       return u2.getFollowers().size() - u1.getFollowers().size();
                   })
                   .collect(Collectors.toList());
    }

    public List<User> getPopularUsers(int limit) {
        return users.values().stream()
                   .filter(User::isActive)
                   .sorted((u1, u2) -> u2.getFollowers().size() - u1.getFollowers().size())
                   .limit(limit)
                   .collect(Collectors.toList());
    }

    public List<User> getVerifiedUsers() {
        return users.values().stream()
                   .filter(User::isVerified)
                   .filter(User::isActive)
                   .sorted((u1, u2) -> u2.getFollowers().size() - u1.getFollowers().size())
                   .collect(Collectors.toList());
    }

    // User Recommendations
    public List<User> getUserRecommendations(String userId) {
        User user = users.get(userId);
        if (user == null) return new ArrayList<>();

        Set<User> recommendations = new HashSet<>();
        
        // Recommend users followed by people the user follows
        for (User followedUser : user.getFollowing()) {
            for (User suggestion : followedUser.getFollowing()) {
                if (!suggestion.equals(user) && !user.getFollowing().contains(suggestion)) {
                    recommendations.add(suggestion);
                }
            }
        }

        // Recommend users with similar music taste
        Map<String, Long> userGenres = user.getTopGenres();
        for (User otherUser : users.values()) {
            if (!otherUser.equals(user) && !user.getFollowing().contains(otherUser)) {
                Map<String, Long> otherGenres = otherUser.getTopGenres();
                
                // Calculate genre similarity
                long commonGenres = userGenres.keySet().stream()
                                             .filter(otherGenres::containsKey)
                                             .count();
                
                if (commonGenres >= 2) { // At least 2 common genres
                    recommendations.add(otherUser);
                }
            }
        }

        return recommendations.stream()
                             .filter(User::isActive)
                             .sorted((u1, u2) -> u2.getFollowers().size() - u1.getFollowers().size())
                             .limit(10)
                             .collect(Collectors.toList());
    }

    // User Analytics
    public Map<String, Object> getUserStatistics(String userId) {
        User user = users.get(userId);
        if (user == null) return new HashMap<>();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlaylists", user.getPlaylists().size());
        stats.put("totalLikedSongs", user.getLikedSongs().size());
        stats.put("totalFollowing", user.getFollowing().size());
        stats.put("totalFollowers", user.getFollowers().size());
        stats.put("totalFollowedArtists", user.getFollowedArtists().size());
        stats.put("totalPlayCount", user.getTotalPlayCount());
        stats.put("topGenres", user.getTopGenres());
        stats.put("topArtists", user.getTopArtists(5));
        stats.put("subscriptionType", user.getSubscription());
        stats.put("isPremium", user.isPremium());
        stats.put("isVerified", user.isVerified());
        stats.put("memberSince", user.getCreatedDate());
        stats.put("lastLogin", user.getLastLoginDate());

        return stats;
    }

    public Map<String, Object> getGlobalUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<User> activeUsers = users.values().stream()
                                     .filter(User::isActive)
                                     .collect(Collectors.toList());
        
        stats.put("totalUsers", users.size());
        stats.put("activeUsers", activeUsers.size());
        stats.put("verifiedUsers", activeUsers.stream().filter(User::isVerified).count());
        stats.put("premiumUsers", activeUsers.stream().filter(User::isPremium).count());
        
        // Subscription distribution
        Map<User.SubscriptionType, Long> subscriptionDist = activeUsers.stream()
            .collect(Collectors.groupingBy(User::getSubscription, Collectors.counting()));
        stats.put("subscriptionDistribution", subscriptionDist);
        
        // Average statistics
        stats.put("averagePlaylistsPerUser", activeUsers.stream()
            .mapToInt(u -> u.getPlaylists().size()).average().orElse(0.0));
        stats.put("averageLikedSongsPerUser", activeUsers.stream()
            .mapToInt(u -> u.getLikedSongs().size()).average().orElse(0.0));
        stats.put("averageFollowersPerUser", activeUsers.stream()
            .mapToInt(u -> u.getFollowers().size()).average().orElse(0.0));

        return stats;
    }

    // User Management
    public boolean deactivateUser(String userId) {
        User user = users.get(userId);
        if (user == null) return false;

        user.setActive(false);
        return true;
    }

    public boolean reactivateUser(String userId) {
        User user = users.get(userId);
        if (user == null) return false;

        user.setActive(true);
        return true;
    }

    public boolean verifyUser(String userId) {
        User user = users.get(userId);
        if (user == null) return false;

        user.setVerified(true);
        return true;
    }

    public boolean deleteUser(String userId) {
        User user = users.remove(userId);
        if (user == null) return false;

        // Remove from indices
        usernameIndex.remove(user.getUsername().toLowerCase());
        emailIndex.remove(user.getEmail().toLowerCase());

        // Clean up social connections
        for (User otherUser : users.values()) {
            otherUser.unfollowUser(user);
        }

        return true;
    }

    // Utility Methods
    public int getTotalUsers() {
        return users.size();
    }

    public int getActiveUsers() {
        return (int) users.values().stream().filter(User::isActive).count();
    }

    public boolean isUsernameAvailable(String username) {
        return username != null && !usernameIndex.containsKey(username.toLowerCase());
    }

    public boolean isEmailAvailable(String email) {
        return email != null && !emailIndex.containsKey(email.toLowerCase());
    }

    // Batch Operations
    public List<User> getUsersByIds(List<String> userIds) {
        return userIds.stream()
                     .map(this::getUserById)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }

    public Map<String, User> getUsersMapByIds(List<String> userIds) {
        return userIds.stream()
                     .collect(Collectors.toMap(
                         id -> id,
                         this::getUserById,
                         (existing, replacement) -> existing,
                         HashMap::new
                     ))
                     .entrySet().stream()
                     .filter(entry -> entry.getValue() != null)
                     .collect(Collectors.toMap(
                         Map.Entry::getKey,
                         Map.Entry::getValue
                     ));
    }
}
