package com.cutm.platform.repositories;

import com.cutm.platform.models.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends MongoRepository<Contest, String> {
    Optional<Contest> findBySlug(String slug);
    List<Contest> findByStatusOrderByStartTimeDesc(String status);
    List<Contest> findByCreatedByOrderByCreatedAtDesc(String createdBy);
    Page<Contest> findByIsPublicTrueAndStatusNot(String status, Pageable pageable);
}
