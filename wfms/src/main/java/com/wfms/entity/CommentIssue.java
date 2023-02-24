package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_issue")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentIssue {
    @Id
    @SequenceGenerator(name = "comment_issue_generator", sequenceName = "comment_issue_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_issue_generator")
    @Column(name = "comment_issue_id")
    private Long commentIssueId;
    @Column(name = "content")
    private String content;
    @Column(name = "file")
    private String file;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "type")
    private String type;


    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

}
