package com.cutm.platform.repositories;

import com.cutm.platform.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findBySlug(String slug);
    Page<Course> findByIsPublishedTrue(Pageable pageable);
    Page<Course> findByLevel(String level, Pageable pageable);
    List<Course> findByCreatedByOrderByCreatedAtDesc(String createdBy);
}
