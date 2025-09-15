package com.musicplayer.repository;

import com.musicplayer.model.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * JSON-based implementation of UserRepository.
 */
public class JsonUserRepository extends JsonRepository<User, String> implements UserRepository {
    
    public JsonUserRepository(String filePath) {
        super(filePath, User.class, User::getId);
    }
    
    public JsonUserRepository() {
        this("data/users.dat");
    }
    
    @Override
    protected User createNewEntity() {
        return new User();
    }
    
    @Override
    protected void validateEntity(User entity) {
        if (entity.getUsername() == null || entity.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("User username cannot be null or empty");
        }
        if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (entity.getDisplayName() == null || entity.getDisplayName().trim().isEmpty()) {
            throw new IllegalArgumentException("User display name cannot be null or empty");
        }
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return findFirstByPredicate(user -> 
            user.getUsername() != null && 
            user.getUsername().equalsIgnoreCase(username.trim())
        );
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return findFirstByPredicate(user -> 
            user.getEmail() != null && 
            user.getEmail().equalsIgnoreCase(email.trim())
        );
    }
    
    @Override
    public List<User> findByDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(user -> 
            user.getDisplayName() != null && 
            user.getDisplayName().toLowerCase().contains(displayName.toLowerCase().trim())
        );
    }
    
    @Override
    public List<User> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return findByPredicate(user -> {
            String searchText = (user.getUsername() + " " + 
                               user.getDisplayName() + " " + 
                               user.getEmail()).toLowerCase();
            return searchText.contains(lowerQuery);
        }).stream()
        .sorted((u1, u2) -> {
            // Prioritize exact username matches
            boolean u1UsernameMatch = u1.getUsername().toLowerCase().equals(lowerQuery);
            boolean u2UsernameMatch = u2.getUsername().toLowerCase().equals(lowerQuery);
            if (u1UsernameMatch && !u2UsernameMatch) return -1;
            if (!u1UsernameMatch && u2UsernameMatch) return 1;
            
            // Then by follower count
            return Integer.compare(u2.getFollowerCount(), u1.getFollowerCount());
        })
        .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findActive() {
        return findByPredicate(User::isActive);
    }
    
    @Override
    public List<User> findVerified() {
        return findByPredicate(User::isVerified);
    }
    
    @Override
    public List<User> findBySubscriptionType(User.SubscriptionType subscriptionType) {
        if (subscriptionType == null) {
            return List.of();
        }
        
        return findByPredicate(user -> 
            user.getSubscriptionType() == subscriptionType
        );
    }
    
    @Override
    public List<User> findPremium() {
        return findByPredicate(user -> 
            user.getSubscriptionType() == User.SubscriptionType.PREMIUM ||
            user.getSubscriptionType() == User.SubscriptionType.FAMILY
        );
    }
    
    @Override
    public List<User> findByFollowerCountGreaterThan(int minFollowers) {
        return findByPredicate(user -> user.getFollowerCount() > minFollowers);
    }
    
    @Override
    public List<User> findFollowersOf(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return List.of();
        }
        
        return findByPredicate(user -> 
            user.getFollowing().contains(userId.trim())
        );
    }
    
    @Override
    public List<User> findFollowingOf(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return List.of();
        }
        
        Optional<User> userOpt = findById(userId.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return findAllById(user.getFollowing());
        }
        
        return List.of();
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return findByUsername(username).isEmpty();
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return findByEmail(email).isEmpty();
    }
    
    @Override
    public List<User> findTopByFollowerCount(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .sorted(Comparator.comparingInt(User::getFollowerCount).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<User> findRecentlyRegistered(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .sorted(Comparator.comparing(User::getRegistrationDate).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public List<User> findRecentlyActive(int limit) {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .filter(user -> user.getLastLoginDate() != null)
                          .sorted(Comparator.comparing(User::getLastLoginDate).reversed())
                          .limit(Math.max(0, limit))
                          .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Additional utility methods
    public long countActive() {
        return countByPredicate(User::isActive);
    }
    
    public long countVerified() {
        return countByPredicate(User::isVerified);
    }
    
    public long countPremium() {
        return countByPredicate(user -> 
            user.getSubscriptionType() == User.SubscriptionType.PREMIUM ||
            user.getSubscriptionType() == User.SubscriptionType.FAMILY
        );
    }
    
    public long countBySubscriptionType(User.SubscriptionType subscriptionType) {
        if (subscriptionType == null) {
            return 0;
        }
        
        return countByPredicate(user -> 
            user.getSubscriptionType() == subscriptionType
        );
    }
    
    public int getTotalFollowerCount() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToInt(User::getFollowerCount)
                          .sum();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int getTotalFollowingCount() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToInt(user -> user.getFollowing().size())
                          .sum();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public double getAverageFollowerCount() {
        lock.readLock().lock();
        try {
            return entities.values().stream()
                          .mapToInt(User::getFollowerCount)
                          .average()
                          .orElse(0.0);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Social network analysis methods
    public List<User> findMutualFollowers(String userId1, String userId2) {
        if (userId1 == null || userId2 == null || userId1.equals(userId2)) {
            return List.of();
        }
        
        List<User> followers1 = findFollowersOf(userId1);
        List<User> followers2 = findFollowersOf(userId2);
        
        return followers1.stream()
                        .filter(followers2::contains)
                        .collect(Collectors.toList());
    }
    
    public List<User> findMutualFollowing(String userId1, String userId2) {
        if (userId1 == null || userId2 == null || userId1.equals(userId2)) {
            return List.of();
        }
        
        List<User> following1 = findFollowingOf(userId1);
        List<User> following2 = findFollowingOf(userId2);
        
        return following1.stream()
                        .filter(following2::contains)
                        .collect(Collectors.toList());
    }
    
    public List<User> suggestFollowing(String userId, int limit) {
        if (userId == null || userId.trim().isEmpty()) {
            return List.of();
        }
        
        Optional<User> userOpt = findById(userId);
        if (userOpt.isEmpty()) {
            return List.of();
        }
        
        User user = userOpt.get();
        List<String> currentFollowing = user.getFollowing();
        
        // Suggest users followed by people the user follows
        return currentFollowing.stream()
                              .flatMap(followedId -> findFollowingOf(followedId).stream())
                              .filter(suggestedUser -> !suggestedUser.getId().equals(userId))
                              .filter(suggestedUser -> !currentFollowing.contains(suggestedUser.getId()))
                              .distinct()
                              .sorted(Comparator.comparingInt(User::getFollowerCount).reversed())
                              .limit(Math.max(0, limit))
                              .collect(Collectors.toList());
    }
}
