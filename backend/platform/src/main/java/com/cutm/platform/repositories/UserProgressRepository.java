package com.cutm.platform.repositories;

import com.cutm.platform.models.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    Optional<UserProgress> findByUserIdAndProblemId(String userId, String problemId);
    List<UserProgress> findByUserId(String userId);
}
