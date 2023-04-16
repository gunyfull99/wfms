package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wfms.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentTaskDTO {
    private Long commentTaskId;
    private Long taskId;
    private String content;
    private Users userId;
    private List<String> files;
    private String type;
    private Integer status;
    private Date createDate;
    private Date updateDate;

}
