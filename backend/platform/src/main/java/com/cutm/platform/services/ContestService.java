package com.cutm.platform.services;

import com.cutm.platform.models.Contest;
import com.cutm.platform.models.Problem;
import com.cutm.platform.repositories.ContestRepository;
import com.cutm.platform.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    public List<Contest> getAllContests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contest> contests = contestRepository.findByIsPublicTrueAndStatusNot("ARCHIVED", pageable);
        return contests.getContent();
    }

    public List<Contest> getUpcomingContests() {
        return contestRepository.findByStatusOrderByStartTimeDesc("UPCOMING");
    }

    public List<Contest> getActiveContests() {
        return contestRepository.findByStatusOrderByStartTimeDesc("ONGOING");
    }

    public Contest getContestById(String contestId) {
        return contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));
    }

    public Contest createContest(Contest contest, String userId) {
        contest.setCreatedAt(LocalDateTime.now());
        contest.setUpdatedAt(LocalDateTime.now());
        contest.setStatus("UPCOMING");
        contest.setRegistrationCount(0);
        contest.setCreatedBy(userId);
        
        if (contest.getParticipantIds() == null) {
            contest.setParticipantIds(new ArrayList<>());
        }
        
        return contestRepository.save(contest);
    }

    public Contest updateContest(String contestId, Contest contestUpdates) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        if (contestUpdates.getTitle() != null) contest.setTitle(contestUpdates.getTitle());
        if (contestUpdates.getDescription() != null) contest.setDescription(contestUpdates.getDescription());
        if (contestUpdates.getStartTime() != null) contest.setStartTime(contestUpdates.getStartTime());
        if (contestUpdates.getEndTime() != null) contest.setEndTime(contestUpdates.getEndTime());
        if (contestUpdates.getProblems() != null) contest.setProblems(contestUpdates.getProblems());

        contest.setUpdatedAt(LocalDateTime.now());
        return contestRepository.save(contest);
    }

    public void deleteContest(String contestId) {
        contestRepository.deleteById(contestId);
    }

    public Contest joinContest(String contestId, String userId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        if (contest.getParticipantIds() == null) {
            contest.setParticipantIds(new ArrayList<>());
        }

        if (!contest.getParticipantIds().contains(userId)) {
            contest.getParticipantIds().add(userId);
            contest.setRegistrationCount(contest.getRegistrationCount() + 1);
            contestRepository.save(contest);
        }

        return contest;
    }

    public void updateContestStatus() {
        List<Contest> allContests = contestRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Contest contest : allContests) {
            if (contest.getStartTime().isAfter(now) && !"UPCOMING".equals(contest.getStatus())) {
                contest.setStatus("UPCOMING");
                contestRepository.save(contest);
            } else if (contest.getStartTime().isBefore(now) && contest.getEndTime().isAfter(now) && !"ONGOING".equals(contest.getStatus())) {
                contest.setStatus("ONGOING");
                contestRepository.save(contest);
            } else if (contest.getEndTime().isBefore(now) && !"ENDED".equals(contest.getStatus())) {
                contest.setStatus("ENDED");
                contestRepository.save(contest);
            }
        }
    }

    public List<Problem> getContestProblems(String contestId) {
        Contest contest = getContestById(contestId);
        List<Problem> problems = new ArrayList<>();
        
        if (contest.getProblems() != null) {
            for (var problemRef : contest.getProblems()) {
                problemRepository.findById(problemRef.getProblemId()).ifPresent(problems::add);
            }
        }
        
        return problems;
    }
    
    public Map<String, Object> getContestWithProblems(String contestId) {
        Contest contest = getContestById(contestId);
        Map<String, Object> result = new HashMap<>();
        result.put("id", contest.getId());
        result.put("title", contest.getTitle());
        result.put("description", contest.getDescription());
        result.put("startTime", contest.getStartTime());
        result.put("endTime", contest.getEndTime());
        result.put("durationMinutes", contest.getDurationMinutes());
        result.put("status", contest.getStatus());
        result.put("registrationCount", contest.getRegistrationCount());
        result.put("isPublic", contest.getIsPublic());
        
        List<Map<String, Object>> problemsList = new ArrayList<>();
        if (contest.getProblems() != null) {
            for (var problemRef : contest.getProblems()) {
                problemRepository.findById(problemRef.getProblemId()).ifPresent(problem -> {
                    Map<String, Object> problemMap = new HashMap<>();
                    problemMap.put("id", problem.getId());
                    problemMap.put("title", problem.getTitle());
                    problemMap.put("difficulty", problem.getDifficulty());
                    problemMap.put("points", problemRef.getPoints());
                    problemMap.put("sequenceNumber", problemRef.getSequenceNumber());
                    problemsList.add(problemMap);
                });
            }
        }
        result.put("problems", problemsList);
        
        return result;
    }
    
    public List<Map<String, Object>> getContestLeaderboard(String contestId) {
        Contest contest = getContestById(contestId);
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        
        if (contest.getStandings() != null) {
            for (var standing : contest.getStandings()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("rank", standing.getRank());
                entry.put("userId", standing.getUserId());
                entry.put("userName", standing.getUserName());
                entry.put("totalPoints", standing.getTotalPoints());
                entry.put("totalTime", standing.getTotalTime());
                leaderboard.add(entry);
            }
        }
        
        return leaderboard;
    }
}

