package com.cutm.platform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "discussions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {

    @Id
    private String id;

    private String problemId;
    private String title;
    private String content;

    private String authorId;
    private String authorName;

    private Integer upvotes;
    private List<String> upvotedBy;
    private Integer downvotes;
    private List<String> downvotedBy;

    private List<Reply> replies;
    private Integer replyCount;

    private List<String> tags;
    private String category;

    private String status;

    private Boolean isPinned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Reply {
    private String id;
    private String authorId;
    private String authorName;
    private String content;

    private Integer upvotes;
    private List<String> upvotedBy;

    private Boolean isAccepted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
