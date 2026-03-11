package com.cutm.platform.services;

import com.cutm.platform.dto.SubmitCodeRequest;
import com.cutm.platform.dto.SubmissionResultDTO;
import com.cutm.platform.dto.TestResultDTO;
import com.cutm.platform.models.*;
import com.cutm.platform.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    @Autowired
    private ProblemService problemService;
    
    private static final String JUDGE0_API = "https://judge0-ce.p.rapidapi.com/submissions";
    
    public SubmissionResultDTO submitCode(String userId, SubmitCodeRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        // Create submission record
        Submission submission = new Submission();
        submission.setUserId(userId);
        submission.setProblemId(request.getProblemId());
        submission.setLanguage(request.getLanguage());
        submission.setSourceCode(request.getSourceCode());
        submission.setStatus("QUEUE"); // Queued for judging
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setIsLatest(true);
        
        // Execute code against test cases (simplified mock)
        executeCode(submission, problem);
        
        // Save submission
        submissionRepository.save(submission);
        
        // Update problem stats
        problemService.incrementSubmission(request.getProblemId());
        
        // Update user progress if accepted
        if (submission.getStatus().equals("ACCEPTED")) {
            problemService.incrementAcceptedSubmission(request.getProblemId());
            updateUserProgress(userId, request.getProblemId(), submission.getId());
            updateUserStats(userId);
        }
        
        return convertToResultDTO(submission);
    }
    
    private void executeCode(Submission submission, Problem problem) {
        // Simplified execution - in production, use Judge0 API
        try {
            // For demo, mock the execution
            List<TestCase> testCases =problem.getTestCases();
            List<TestCaseResult> results = new ArrayList<>();
            
            int passedCount = 0;
            long totalTime = 0;
            
            for (int i = 0; i < testCases.size(); i++) {
                TestCase testCase = testCases.get(i);
                
                // Mock execution result
                boolean passed = Math.random() > 0.2; // 80% pass rate for demo
                
                TestCaseResult result = new TestCaseResult();
                result.setTestCaseNumber(i + 1);
                result.setStatus(passed ? "PASSED" : "FAILED");
                result.setExpectedOutput(testCase.getExpectedOutput());
                result.setActualOutput(passed ? testCase.getExpectedOutput() : "Wrong output");
                result.setExecutionTime((long) (Math.random() * 1000)); // 0-1000ms
                
                results.add(result);
                totalTime += result.getExecutionTime();
                
                if (passed) passedCount++;
            }
            
            submission.setTestResults(results);
            submission.setTestCasesPassed(passedCount);
            submission.setTotalTestCases(testCases.size());
            submission.setExecutionTime(totalTime);
            submission.setMemoryUsed((long) (Math.random() * 50000000)); // 0-50MB
            
            if (passedCount == testCases.size()) {
                submission.setStatus("ACCEPTED");
                submission.setPoints(getProblemPoints(problem.getDifficulty()));
            } else {
                submission.setStatus("WRONG_ANSWER");
                submission.setPoints(0);
            }
            
        } catch (Exception e) {
            submission.setStatus("RUNTIME_ERROR");
            submission.setRuntimeError(e.getMessage());
            submission.setPoints(0);
        }
        
        submission.setJudgedAt(LocalDateTime.now());
    }
    
    private int getProblemPoints(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY": return 10;
            case "MEDIUM": return 25;
            case "HARD": return 50;
            default: return 5;
        }
    }
    
    private void updateUserProgress(String userId, String problemId, String submissionId) {
        UserProgress progress = userProgressRepository.findByUserIdAndProblemId(userId, problemId)
                .orElse(new UserProgress());
        
        progress.setUserId(userId);
        progress.setProblemId(problemId);
        progress.setStatus("SOLVED");
        progress.setFirstSolvedAt(LocalDateTime.now());
        progress.setLastSubmissionId(submissionId);
        progress.setLastSubmissionTime(LocalDateTime.now());
        progress.setUpdatedAt(LocalDateTime.now());
        
        if (progress.getId() == null) {
            progress.setCreatedAt(LocalDateTime.now());
            progress.setAttempts(1);
            progress.setPoints(50); // Full points for first solve
        } else {
            progress.setAttempts(progress.getAttempts() + 1);
        }
        
        userProgressRepository.save(progress);
    }
    
    private void updateUserStats(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Count solved problems
        long solvedCount = userProgressRepository.findAll().stream()
                .filter(up -> up.getUserId().equals(userId) && "SOLVED".equals(up.getStatus()))
                .count();
        
        user.setProblemsSolved((int) solvedCount);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }
    
    public SubmissionResultDTO getSubmissionResult(String submissionId, String userId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        // Verify ownership
        if (!submission.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }
        
        return convertToResultDTO(submission);
    }
    
    public List<Submission> getUserSubmissions(String userId, Pageable pageable) {
        return submissionRepository.findByUserId(userId, pageable).getContent();
    }
    
    public List<Submission> getProblemSubmissions(String problemId, Pageable pageable) {
        return submissionRepository.findByProblemId(problemId, pageable).getContent();
    }
    
    public long getUserSolvedCount(String userId) {
        return submissionRepository.countByUserIdAndStatus(userId, "ACCEPTED");
    }
    
    private SubmissionResultDTO convertToResultDTO(Submission submission) {
        return new SubmissionResultDTO(
                submission.getId(),
                submission.getStatus(),
                submission.getTestCasesPassed(),
                submission.getTotalTestCases(),
                submission.getExecutionTime(),
                submission.getMemoryUsed(),
                submission.getOutput(),
                submission.getRuntimeError() != null ? submission.getRuntimeError() : submission.getCompileError(),
                submission.getTestResults().stream()
                        .map(tr -> new TestResultDTO(tr.getTestCaseNumber(), tr.getStatus(), tr.getExpectedOutput(), tr.getActualOutput()))
                        .collect(Collectors.toList()),
                submission.getPoints()
        );
    }
}
