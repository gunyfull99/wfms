package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteDTO {
    private Long noteId;
    private Date createDate;
    private Date updateDate;
    private String content;
    private String title;
    private Integer status;
    private ProjectDTO projectDTO;
}
