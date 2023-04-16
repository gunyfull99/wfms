package com.wfms.service;

import com.wfms.Dto.CommentTaskDTO;
import com.wfms.entity.CommentTask;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommentTaskService {
    List<CommentTask> findAll();
    Page<CommentTask> findWithPage(int page, int total);
    List<CommentTask> findCommentTaskByTaskId(Long taskId);
    CommentTaskDTO createComment(String commentTaskDTO, List<MultipartFile> image);
    String getUrlFile(String name);
    byte[] getFile(String name);
    List<CommentTaskDTO> getCommentByTask(Long taskId);
}
