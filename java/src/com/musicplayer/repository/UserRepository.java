package com.musicplayer.repository;

import com.musicplayer.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entities with domain-specific query methods.
 */
public interface UserRepository extends Repository<User, String> {
    
    /**
     * Find user by username.
     * @param username The username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email.
     * @param email The email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find users by display name (case-insensitive).
     * @param displayName The display name
     * @return List of users with matching display name
     */
    List<User> findByDisplayName(String displayName);
    
    /**
     * Search users by query (searches username, display name, email).
     * @param query The search query
     * @return List of matching users
     */
    List<User> search(String query);
    
    /**
     * Find active users only.
     * @return List of active users
     */
    List<User> findActive();
    
    /**
     * Find verified users only.
     * @return List of verified users
     */
    List<User> findVerified();
    
    /**
     * Find users by subscription type.
     * @param subscriptionType The subscription type
     * @return List of users with the specified subscription
     */
    List<User> findBySubscriptionType(User.SubscriptionType subscriptionType);
    
    /**
     * Find premium users (Premium or Family subscription).
     * @return List of premium users
     */
    List<User> findPremium();
    
    /**
     * Find users with follower count greater than specified value.
     * @param minFollowers Minimum follower count
     * @return List of popular users
     */
    List<User> findByFollowerCountGreaterThan(int minFollowers);
    
    /**
     * Find users who follow a specific user.
     * @param userId The user ID to check followers for
     * @return List of users who follow the specified user
     */
    List<User> findFollowersOf(String userId);
    
    /**
     * Find users followed by a specific user.
     * @param userId The user ID to check following for
     * @return List of users followed by the specified user
     */
    List<User> findFollowingOf(String userId);
    
    /**
     * Check if username is available.
     * @param username The username to check
     * @return true if username is available, false otherwise
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Check if email is available.
     * @param email The email to check
     * @return true if email is available, false otherwise
     */
    boolean isEmailAvailable(String email);
    
    /**
     * Get users ordered by follower count (descending).
     * @param limit Maximum number of users to return
     * @return List of most followed users
     */
    List<User> findTopByFollowerCount(int limit);
    
    /**
     * Get recently registered users.
     * @param limit Maximum number of users to return
     * @return List of recently registered users
     */
    List<User> findRecentlyRegistered(int limit);
    
    /**
     * Get recently active users (by last login).
     * @param limit Maximum number of users to return
     * @return List of recently active users
     */
    List<User> findRecentlyActive(int limit);
}
