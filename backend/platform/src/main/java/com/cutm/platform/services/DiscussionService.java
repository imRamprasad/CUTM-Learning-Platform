package com.cutm.platform.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cutm.platform.models.Discussion;
import com.cutm.platform.repositories.DiscussionRepository;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    public List<Discussion> getAllDiscussions() {
        return discussionRepository.findAll();
    }

    public List<Discussion> getProblemDiscussions(String problemId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return discussionRepository.findByProblemIdAndStatusOrderByUpvotesDesc(problemId, "ACTIVE", pageable);
    }

    public Discussion getById(String id) {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));
    }

    public Discussion createDiscussion(Discussion discussion, String userId, String authorName) {
        LocalDateTime now = LocalDateTime.now();
        discussion.setId(null);
        discussion.setAuthorId(userId);
        discussion.setAuthorName(authorName);
        discussion.setCreatedAt(now);
        discussion.setUpdatedAt(now);
        if (discussion.getStatus() == null) {
            discussion.setStatus("ACTIVE");
        }
        if (discussion.getUpvotes() == null) {
            discussion.setUpvotes(0);
        }
        if (discussion.getDownvotes() == null) {
            discussion.setDownvotes(0);
        }
        if (discussion.getReplyCount() == null) {
            discussion.setReplyCount(0);
        }
        if (discussion.getIsPinned() == null) {
            discussion.setIsPinned(false);
        }
        return discussionRepository.save(discussion);
    }

    public Discussion updateDiscussion(String id, Discussion incoming) {
        Discussion existing = getById(id);
        existing.setTitle(incoming.getTitle());
        existing.setContent(incoming.getContent());
        existing.setTags(incoming.getTags());
        existing.setCategory(incoming.getCategory());
        existing.setUpdatedAt(LocalDateTime.now());
        return discussionRepository.save(existing);
    }

    public void deleteDiscussion(String id) {
        discussionRepository.deleteById(id);
    }
}
