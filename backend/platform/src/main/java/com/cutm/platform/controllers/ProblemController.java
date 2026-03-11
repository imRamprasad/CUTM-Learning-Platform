package com.cutm.platform.controllers;

import com.cutm.platform.dto.*;
import com.cutm.platform.models.Problem;
import com.cutm.platform.services.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problems")
@CrossOrigin(origins = "http://localhost:3000")
public class ProblemController {
    
    @Autowired
    private ProblemService problemService;
    
    @GetMapping
    public ResponseEntity<Page<ProblemDTO>> getAllProblems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProblemDTO> problems = problemService.getAllProblems(pageable);
        return ResponseEntity.ok(problems);
    }
    
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<Page<ProblemDTO>> getProblemsByDifficulty(
            @PathVariable String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProblemDTO> problems = problemService.getProblemsByDifficulty(difficulty, pageable);
        return ResponseEntity.ok(problems);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ProblemDTO>> searchProblems(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProblemDTO> problems = problemService.searchProblems(query, pageable);
        return ResponseEntity.ok(problems);
    }
    
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemDetailDTO> getProblemDetails(
            @PathVariable String problemId,
            Authentication authentication) {
        
        String userId = authentication != null ? authentication.getPrincipal().toString() : null;
        ProblemDetailDTO problem = problemService.getProblemDetails(problemId, userId);
        return ResponseEntity.ok(problem);
    }
    
    // Admin endpoints
    @PostMapping
    public ResponseEntity<Problem> createProblem(@RequestBody Problem problem, Authentication authentication) {
        // Only ADMIN role can create problems
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Problem created = problemService.createProblem(problem);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{problemId}")
    public ResponseEntity<Problem> updateProblem(
            @PathVariable String problemId,
            @RequestBody Problem problem,
            Authentication authentication) {
        
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Problem updated = problemService.updateProblem(problemId, problem);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{problemId}")
    public ResponseEntity<Void> deleteProblem(
            @PathVariable String problemId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        problemService.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{problemId}/publish")
    public ResponseEntity<Void> publishProblem(
            @PathVariable String problemId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        problemService.publishProblem(problemId);
        return ResponseEntity.ok().build();
    }
}
