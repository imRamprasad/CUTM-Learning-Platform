package com.cutm.platform.repositories;

import com.cutm.platform.models.Discussion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends MongoRepository<Discussion, String> {
    List<Discussion> findByProblemIdAndStatusOrderByUpvotesDesc(String problemId, String status, Pageable pageable);
    List<Discussion> findByAuthorIdOrderByCreatedAtDesc(String authorId);

    @Query("{'$text': {'$search': ?0}}")
    List<Discussion> searchByTitle(String searchTerm);
}
