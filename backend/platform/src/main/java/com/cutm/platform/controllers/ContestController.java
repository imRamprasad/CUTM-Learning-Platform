package com.cutm.platform.controllers;

import com.cutm.platform.models.Contest;
import com.cutm.platform.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contests")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping
    public ResponseEntity<List<Contest>> getAllContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Contest> contests = contestService.getAllContests(page, size);
        return ResponseEntity.ok(contests);
    }

    @GetMapping("/{contestId}")
    public ResponseEntity<Map<String, Object>> getContestDetails(@PathVariable String contestId) {
        Map<String, Object> contest = contestService.getContestWithProblems(contestId);
        return ResponseEntity.ok(contest);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Contest>> getActiveContests() {
        List<Contest> contests = contestService.getActiveContests();
        return ResponseEntity.ok(contests);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Contest>> getUpcomingContests() {
        List<Contest> contests = contestService.getUpcomingContests();
        return ResponseEntity.ok(contests);
    }

    @PostMapping
    public ResponseEntity<Contest> createContest(
            @RequestBody Contest contest,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        Contest created = contestService.createContest(contest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{contestId}")
    public ResponseEntity<Contest> updateContest(
            @PathVariable String contestId,
            @RequestBody Contest contest,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Contest updated = contestService.updateContest(contestId, contest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{contestId}")
    public ResponseEntity<Void> deleteContest(
            @PathVariable String contestId,
            Authentication authentication) {
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        contestService.deleteContest(contestId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{contestId}/join")
    public ResponseEntity<Contest> joinContest(
            @PathVariable String contestId,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        Contest contest = contestService.joinContest(contestId, userId);
        return ResponseEntity.ok(contest);
    }

    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<List<Map<String, Object>>> getContestLeaderboard(
            @PathVariable String contestId) {
        List<Map<String, Object>> leaderboard = contestService.getContestLeaderboard(contestId);
        return ResponseEntity.ok(leaderboard);
    }
}

