package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wfms.entity.Issue;
import com.wfms.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentIssueDTO {
    private Long commentIssueId;
    private Long issueId;
    private String content;
    private Users userId;
    private List<String> files;
    private String type;
    private Integer status;
    private Date createDate;
    private Date updateDate;

}
