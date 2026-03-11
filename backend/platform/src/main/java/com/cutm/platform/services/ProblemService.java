package com.cutm.platform.services;

import com.cutm.platform.dto.ProblemDTO;
import com.cutm.platform.dto.ProblemDetailDTO;
import com.cutm.platform.dto.CodeTemplateDTO;
import com.cutm.platform.dto.TestCaseDTO;
import com.cutm.platform.models.Problem;
import com.cutm.platform.models.CodeTemplate;
import com.cutm.platform.models.TestCase;
import com.cutm.platform.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {
    
    @Autowired
    private ProblemRepository problemRepository;
    
    public Page<ProblemDTO> getAllProblems(Pageable pageable) {
        return problemRepository.findByIsPublishedTrue(pageable)
                .map(this::convertToDTO);
    }
    
    public Page<ProblemDTO> getProblemsByDifficulty(String difficulty, Pageable pageable) {
        return problemRepository.findByDifficultyOrderByTotalSubmissionsDesc(difficulty, pageable)
            .map(this::convertToDTO);
    }
    
    public Page<ProblemDTO> searchProblems(String searchTerm, Pageable pageable) {
        return problemRepository.searchByTitle(searchTerm, pageable)
                .map(this::convertToDTO);
    }
    
    public ProblemDetailDTO getProblemDetails(String problemId, String userId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        ProblemDetailDTO dto = new ProblemDetailDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setDifficulty(problem.getDifficulty());
        dto.setProblemStatement(problem.getProblemStatement());
        dto.setHints(problem.getHints() != null ? List.of(problem.getHints()) : List.of());
        dto.setTimeLimit(problem.getTimeLimit());
        dto.setMemoryLimit(problem.getMemoryLimit());
        dto.setTotalSubmissions(problem.getTotalSubmissions());
        dto.setTotalAccepted(problem.getTotalAccepted());
        dto.setEditorial(problem.getEditorial());
        dto.setSupportedLanguages(problem.getSupportedLanguages());
        
        // Convert code templates
        if (problem.getCodeTemplates() != null) {
            dto.setCodeTemplates(problem.getCodeTemplates().stream()
                    .map(ct -> new CodeTemplateDTO(ct.getLanguage(), ct.getBoilerplate()))
                    .collect(Collectors.toList()));
        }
        
        // Only show non-hidden test cases
        if (problem.getTestCases() != null) {
            dto.setTestCases(problem.getTestCases().stream()
                    .map(tc -> new TestCaseDTO(tc.getInput(), tc.getExpectedOutput(), tc.getExplanation()))
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public Problem createProblem(Problem problem) {
        problem.setCreatedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());
        problem.setTotalSubmissions(0);
        problem.setTotalAccepted(0);
        problem.setAcceptanceRate(0.0);
        problem.setStatus("DRAFT");
        return problemRepository.save(problem);
    }
    
    public Problem updateProblem(String problemId, Problem problemUpdates) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        if (problemUpdates.getTitle() != null) problem.setTitle(problemUpdates.getTitle());
        if (problemUpdates.getDescription() != null) problem.setDescription(problemUpdates.getDescription());
        if (problemUpdates.getDifficulty() != null) problem.setDifficulty(problemUpdates.getDifficulty());
        if (problemUpdates.getTestCases() != null) problem.setTestCases(problemUpdates.getTestCases());
        if (problemUpdates.getCodeTemplates() != null) problem.setCodeTemplates(problemUpdates.getCodeTemplates());
        
        problem.setUpdatedAt(LocalDateTime.now());
        return problemRepository.save(problem);
    }
    
    public void deleteProblem(String problemId) {
        problemRepository.deleteById(problemId);
    }
    
    public void publishProblem(String problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setIsPublished(true);
        problem.setStatus("PUBLISHED");
        problem.setUpdatedAt(LocalDateTime.now());
        problemRepository.save(problem);
    }
    
    public void incrementSubmission(String problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setTotalSubmissions(problem.getTotalSubmissions() + 1);
        problemRepository.save(problem);
    }
    
    public void incrementAcceptedSubmission(String problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setTotalAccepted(problem.getTotalAccepted() + 1);
        
        // Update acceptance rate
        if (problem.getTotalSubmissions() > 0) {
            problem.setAcceptanceRate((double) problem.getTotalAccepted() / problem.getTotalSubmissions() * 100);
        }
        
        problemRepository.save(problem);
    }
    
    private ProblemDTO convertToDTO(Problem problem) {
        return new ProblemDTO(
                problem.getId(),
                problem.getTitle(),
                problem.getSlug(),
                problem.getDifficulty(),
                problem.getCategories(),
                problem.getTags(),
                problem.getTotalSubmissions(),
                problem.getTotalAccepted(),
                problem.getAcceptanceRate(),
                false // will be set based on user's submissions
        );
    }
}
