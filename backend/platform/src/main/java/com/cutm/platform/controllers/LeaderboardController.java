package com.cutm.platform.controllers;

import com.cutm.platform.services.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<Map<String, Object>> leaderboard = leaderboardService.getLeaderboard(page, size);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Map<String, Object>>> getTop10() {
        List<Map<String, Object>> leaderboard = leaderboardService.getTop10();
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/my-rank")
    public ResponseEntity<Map<String, Object>> getMyRank(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        Map<String, Object> rank = leaderboardService.getUserRank(userId);
        return ResponseEntity.ok(rank);
    }
}

