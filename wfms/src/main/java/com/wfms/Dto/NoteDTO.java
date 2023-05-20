package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

 import java.time.LocalDateTime; 

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteDTO {
    private Long noteId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String content;
    private String title;
    private Integer status;
    private ProjectDTO projectDTO;
}
