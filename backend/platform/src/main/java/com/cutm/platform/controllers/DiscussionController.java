package com.cutm.platform.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cutm.platform.models.Discussion;
import com.cutm.platform.models.User;
import com.cutm.platform.repositories.UserRepository;
import com.cutm.platform.services.DiscussionService;

@RestController
@RequestMapping("/api/discussions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Discussion>> getAllDiscussions() {
        return ResponseEntity.ok(discussionService.getAllDiscussions());
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<Discussion>> getProblemDiscussions(
            @PathVariable String problemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(discussionService.getProblemDiscussions(problemId, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discussion> getById(@PathVariable String id) {
        return ResponseEntity.ok(discussionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Discussion> createDiscussion(@RequestBody Discussion discussion, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = authentication.getPrincipal().toString();
        User user = userRepository.findById(userId).orElse(null);
        String authorName = user != null ? user.getUsername() : "user";

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(discussionService.createDiscussion(discussion, userId, authorName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discussion> updateDiscussion(
            @PathVariable String id,
            @RequestBody Discussion discussion,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(discussionService.updateDiscussion(id, discussion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable String id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        discussionService.deleteDiscussion(id);
        return ResponseEntity.noContent().build();
    }
}
