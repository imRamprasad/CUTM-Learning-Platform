package com.cutm.platform.controllers;

import com.cutm.platform.dto.*;
import com.cutm.platform.models.Submission;
import com.cutm.platform.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/submissions")
@CrossOrigin(origins = "http://localhost:3000")
public class SubmissionController {
    
    @Autowired
    private SubmissionService submissionService;
    
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
}
