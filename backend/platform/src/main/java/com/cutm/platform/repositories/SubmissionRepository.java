package com.cutm.platform.repositories;

import com.cutm.platform.models.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByUserIdAndProblemIdOrderBySubmittedAtDesc(String userId, String problemId);
    Page<Submission> findByUserId(String userId, Pageable pageable);
    Page<Submission> findByProblemId(String problemId, Pageable pageable);
    List<Submission> findByUserIdAndIsLatestTrue(String userId);
    Long countByUserIdAndStatus(String userId, String status);
    Long countByProblemIdAndStatus(String problemId, String status);
}
