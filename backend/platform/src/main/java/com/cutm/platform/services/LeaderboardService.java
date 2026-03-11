package com.cutm.platform.services;

import com.cutm.platform.models.User;
import com.cutm.platform.models.UserProgress;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.repositories.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    // Point system
    private static final int MEDIUM_POINTS = 20;

    public List<Map<String, Object>> getLeaderboard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = userRepository.findAll(pageable).getContent();
        
        return users.stream()
            .map(user -> calculateUserPoints(user))
            .sorted((a, b) -> ((Integer) b.get("points")).compareTo((Integer) a.get("points")))
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTop10() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
            .map(user -> calculateUserPoints(user))
            .sorted((a, b) -> ((Integer) b.get("points")).compareTo((Integer) a.get("points")))
            .limit(10)
            .collect(Collectors.toList());
    }

    private Map<String, Object> calculateUserPoints(User user) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("userId", user.getId());
        entry.put("username", user.getUsername());
        entry.put("firstName", user.getFirstName());
        entry.put("lastName", user.getLastName());
        
        // Calculate points based on solved problems
        List<UserProgress> progressList = userProgressRepository.findByUserId(user.getId());
        int totalPoints = 0;
        int solvedCount = 0;
        
        for (UserProgress progress : progressList) {
            if ("SOLVED".equals(progress.getStatus())) {
                solvedCount++;
                // Get difficulty from problem and add points
                totalPoints += MEDIUM_POINTS; // Default to medium
            }
        }
        
        entry.put("points", totalPoints);
        entry.put("solvedCount", solvedCount);
        Integer currentRank = user.getRank();
        entry.put("rank", currentRank != null ? currentRank : 0);
        
        return entry;
    }

    public Map<String, Object> getUserRank(String userId) {
        String resolvedUserId = Objects.requireNonNull(userId, "userId");
        User user = userRepository.findById(resolvedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Map<String, Object> userPoints = calculateUserPoints(user);
        
        // Calculate rank
        List<User> allUsers = userRepository.findAll();
        long rank = allUsers.stream()
            .map(this::calculateUserPoints)
            .filter(u -> ((Integer) u.get("points")) > ((Integer) userPoints.get("points")))
            .count();
        
        userPoints.put("rank", rank + 1);
        
        return userPoints;
    }

    public void updateUserRank(String userId) {
        String resolvedUserId = Objects.requireNonNull(userId, "userId");
        User user = userRepository.findById(resolvedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Map<String, Object> userPoints = calculateUserPoints(user);
        
        // Calculate rank
        List<User> allUsers = userRepository.findAll();
        long rank = allUsers.stream()
            .map(this::calculateUserPoints)
            .filter(u -> ((Integer) u.get("points")) > ((Integer) userPoints.get("points")))
            .count();
        
        user.setRank((int) rank + 1);
        user.setTotalPoints((Integer) userPoints.get("points"));
        user.setProblemsSolved((Integer) userPoints.get("solvedCount"));
        
        userRepository.save(user);
    }
}

