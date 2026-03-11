package com.cutm.platform.repositories;

import com.cutm.platform.models.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    Optional<Problem> findBySlug(String slug);
    Page<Problem> findByDifficultyOrderByTotalSubmissionsDesc(String difficulty, Pageable pageable);
    Page<Problem> findByIsPublishedTrue(Pageable pageable);

    @Query("{'categories': ?0, 'isPublished': true}")
    Page<Problem> findByCategory(String category, Pageable pageable);

    @Query("{'$text': {'$search': ?0}, 'isPublished': true}")
    Page<Problem> searchByTitle(String searchTerm, Pageable pageable);

    List<Problem> findByCreatedByAndStatusOrderByCreatedAtDesc(String createdBy, String status);
}
