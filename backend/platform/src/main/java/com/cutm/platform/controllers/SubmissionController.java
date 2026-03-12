package com.cutm.platform.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cutm.platform.dto.AdminSubmissionDTO;
import com.cutm.platform.dto.SubmissionResultDTO;
import com.cutm.platform.dto.SubmitCodeRequest;
import com.cutm.platform.models.Problem;
import com.cutm.platform.models.Submission;
import com.cutm.platform.models.User;
import com.cutm.platform.repositories.ProblemRepository;
import com.cutm.platform.repositories.SubmissionRepository;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.services.SubmissionService;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class SubmissionController {

    private static final DateTimeFormatter SUBMISSION_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;
    
    @PostMapping("/submit")
    public ResponseEntity<SubmissionResultDTO> submitCode(
            @RequestBody SubmitCodeRequest request,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        SubmissionResultDTO result = submissionService.submitCode(userId, request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResultDTO> getSubmissionResult(
            @PathVariable String submissionId,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        SubmissionResultDTO result = submissionService.getSubmissionResult(submissionId, userId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/user/submissions")
    public ResponseEntity<List<Submission>> getUserSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        String userId = authentication.getPrincipal().toString();
        Pageable pageable = PageRequest.of(page, size);
        List<Submission> submissions = submissionService.getUserSubmissions(userId, pageable);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<Submission>> getProblemSubmissions(
            @PathVariable String problemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<Submission> submissions = submissionService.getProblemSubmissions(problemId, pageable);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/user/solved-count")
    public ResponseEntity<Long> getUserSolvedCount(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        long count = submissionService.getUserSolvedCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<AdminSubmissionDTO>> getAdminSubmissions(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
        Page<Submission> submissionsPage;
        if (userId != null && !userId.isBlank() && status != null && !status.isBlank()) {
            submissionsPage = submissionRepository.findByUserIdAndStatus(userId, status.toUpperCase(), pageable);
        } else if (userId != null && !userId.isBlank()) {
            submissionsPage = submissionRepository.findByUserId(userId, pageable);
        } else if (status != null && !status.isBlank()) {
            submissionsPage = submissionRepository.findByStatus(status.toUpperCase(), pageable);
        } else {
            submissionsPage = submissionRepository.findAll(pageable);
        }

        Map<String, String> userNames = buildUserNames(submissionsPage.getContent());
        Map<String, String> problemTitles = buildProblemTitles(submissionsPage.getContent());

        return ResponseEntity.ok(submissionsPage.getContent().stream()
                .map(submission -> toAdminSubmissionDTO(submission, userNames, problemTitles))
                .collect(Collectors.toList()));
    }

    @GetMapping("/admin/summary")
    public ResponseEntity<Map<String, Long>> getAdminSubmissionSummary(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Submission> all = submissionRepository.findAll();
        Map<String, String> userNames = buildUserNames(all);
        Map<String, String> problemTitles = buildProblemTitles(all);
        List<AdminSubmissionDTO> adminSubmissions = all.stream()
                .map(submission -> toAdminSubmissionDTO(submission, userNames, problemTitles))
                .collect(Collectors.toList());

        long plagiarismDetected = adminSubmissions.stream().filter(AdminSubmissionDTO::getPlagiarismDetected).count();
        long cheatingFlagged = adminSubmissions.stream().filter(submission -> !submission.getCheatingFlags().isEmpty()).count();
        long highRisk = all.stream().filter(submission -> submission.getStatus() != null
                && (submission.getStatus().equalsIgnoreCase("RUNTIME_ERROR")
                || submission.getStatus().equalsIgnoreCase("COMPILATION_ERROR"))).count();

        return ResponseEntity.ok(Map.of(
                "total", (long) all.size(),
                "plagiarismDetected", plagiarismDetected,
                "cheatingFlagged", cheatingFlagged,
                "highRisk", highRisk
        ));
    }

    private Map<String, String> buildUserNames(List<Submission> submissions) {
        List<String> userIds = submissions.stream()
                .map(Submission::getUserId)
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .collect(Collectors.toList());
        Iterable<String> safeUserIds = new ArrayList<>(userIds);

        Map<String, String> userNames = new HashMap<>();
        List<User> users = userRepository.findAllById(safeUserIds);
        for (User user : users) {
            String displayName = (user.getFirstName() != null && !user.getFirstName().isBlank()) || (user.getLastName() != null && !user.getLastName().isBlank())
                    ? ((user.getFirstName() == null ? "" : user.getFirstName()) + " " + (user.getLastName() == null ? "" : user.getLastName())).trim()
                    : user.getUsername();
            userNames.put(user.getId(), displayName == null || displayName.isBlank() ? user.getId() : displayName);
        }
        return userNames;
    }

    private Map<String, String> buildProblemTitles(List<Submission> submissions) {
        List<String> problemIds = submissions.stream()
                .map(Submission::getProblemId)
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .collect(Collectors.toList());
        Iterable<String> safeProblemIds = new ArrayList<>(problemIds);

        Map<String, String> problemTitles = new HashMap<>();
        List<Problem> problems = problemRepository.findAllById(safeProblemIds);
        for (Problem problem : problems) {
            problemTitles.put(problem.getId(), problem.getTitle() == null || problem.getTitle().isBlank() ? problem.getId() : problem.getTitle());
        }
        return problemTitles;
    }

    private AdminSubmissionDTO toAdminSubmissionDTO(Submission submission, Map<String, String> userNames, Map<String, String> problemTitles) {
        List<String> cheatingFlags = new ArrayList<>();
        String status = submission.getStatus() == null ? "" : submission.getStatus().toUpperCase();

        if (submission.getContestId() != null && !submission.getContestId().isBlank() && submission.getSubmissionTime() != null && submission.getSubmissionTime() < 15) {
            cheatingFlags.add("SUSPICIOUS_TIMING");
        }
        if ("COMPILATION_ERROR".equals(status) || "RUNTIME_ERROR".equals(status)) {
            cheatingFlags.add("TOOLING_SIGNATURE");
        }

        int plagiarismScore = cheatingFlags.isEmpty() ? 0 : Math.min(95, cheatingFlags.size() * 35);
        boolean plagiarismDetected = plagiarismScore >= 60;

        return new AdminSubmissionDTO(
                submission.getId(),
                submission.getUserId(),
                userNames.getOrDefault(submission.getUserId(), submission.getUserId()),
                submission.getProblemId(),
                problemTitles.getOrDefault(submission.getProblemId(), submission.getProblemId()),
                submission.getLanguage() == null ? "unknown" : submission.getLanguage(),
                normalizeVerdict(submission),
                formatSubmittedAt(submission.getSubmittedAt()),
                plagiarismScore,
                plagiarismDetected,
                cheatingFlags,
                submission.getContestId(),
                "fp-" + submission.getId()
        );
    }

    private String normalizeVerdict(Submission submission) {
        String value = submission.getStatus();
        if (value == null || value.isBlank()) {
            value = submission.getVerdict();
        }
        if (value == null) {
            return "Wrong Answer";
        }

        return switch (value.toUpperCase()) {
            case "ACCEPTED", "PASS" -> "Accepted";
            case "TIME_LIMIT_EXCEEDED" -> "Time Limit Exceeded";
            case "RUNTIME_ERROR", "COMPILATION_ERROR" -> "Runtime Error";
            default -> "Wrong Answer";
        };
    }

    private String formatSubmittedAt(LocalDateTime submittedAt) {
        return submittedAt != null ? submittedAt.format(SUBMISSION_TIME_FORMAT) : "";
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
