package com.wfms.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfms.Dto.CommentTaskDTO;
import com.wfms.entity.CommentTask;
import com.wfms.entity.Task;
import com.wfms.entity.Users;
import com.wfms.repository.CommentTaskRepository;
import com.wfms.repository.TaskRepository;
import com.wfms.service.CommentTaskService;
import com.wfms.service.UsersService;
import com.wfms.utils.MinioUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Log4j2
public class CommentTaskServiceImpl implements CommentTaskService {
    @Autowired
    private CommentTaskRepository commentTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private MinioUtils minioUtils;
    @Override
    public List<CommentTask> findAll() {
        return commentTaskRepository.findAll();
    }

    @Override
    public Page<CommentTask> findWithPage(int page, int total) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return commentTaskRepository.findAll(pageable);
    }

    @Override
    public List<CommentTask> findCommentTaskByTaskId(Long taskId) {
        return commentTaskRepository.findByTaskId(taskId);
    }

    @Override
    public CommentTaskDTO createComment(String comment, List<MultipartFile> images) {
        CommentTaskDTO commentTaskDTO =new CommentTaskDTO();
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            commentTaskDTO =objectMapper.readValue(comment, CommentTaskDTO.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        Assert.notNull(commentTaskDTO.getContent(),"Content must not be null");
        Assert.notNull(commentTaskDTO.getTaskId(),"TaskId must not be null");
        Assert.notNull(commentTaskDTO.getUserId(),"UserId() must not be null");
        Task taskData = taskRepository.getById(commentTaskDTO.getTaskId());
        Assert.notNull(taskData,"Not found TaskId "+ commentTaskDTO.getTaskId());
        Users users =usersService.findById(commentTaskDTO.getUserId().getId());
        Assert.notNull(users,"Not found userId "+ commentTaskDTO.getUserId().getId());
        String filenames ="" ;
        String filename =null ;
        CommentTask commentTask = new CommentTask();
        BeanUtils.copyProperties(commentTaskDTO, commentTask);
        if(Objects.nonNull(images) && !images.isEmpty()){
            for (int i = 0; i <images.size() ; i++) {
                filename= StringUtils.cleanPath((Objects.requireNonNull(images.get(i).getOriginalFilename())));
                filename = filename.substring(0, filename.lastIndexOf("."))
                        .replace(".", "").replace(";","-") + "." + filename.substring(filename.lastIndexOf(".") + 1);

                filename=minioUtils.uploadFile(filename,images.get(i));
                filenames += (i!=images.size()-1 ? filename+";" : filename);
            }
            commentTask.setFile(filenames);
        }
        commentTask.setCommentTaskId(null);
        commentTask.setCreateDate(new Date());
        commentTask.setStatus(1);
        commentTask.setTask(com.wfms.entity.Task.builder().taskId(commentTaskDTO.getTaskId()).build());
        commentTask.setUserId(commentTaskDTO.getUserId().getId());
        commentTask = commentTaskRepository.save(commentTask);
        BeanUtils.copyProperties(commentTask, commentTaskDTO);

        return commentTaskDTO;
    }

    @Override
    public String getUrlFile(String name) {
        return minioUtils.getFileUrl(name);
    }

    @Override
    public byte[] getFile(String name) {
        return minioUtils.getFile(name);
    }

    @Override
    public List<CommentTaskDTO> getCommentByTask(Long taskId) {
        Assert.notNull(taskId,"TaskId must not be null");
        Task taskData = taskRepository.getById(taskId);
        Assert.notNull(taskData,"Not found TaskId "+ taskId);
        List<CommentTask> commentTasks = commentTaskRepository.findByTaskId(taskId);
        List<CommentTaskDTO> commentTaskDTOList =new ArrayList<>();
        if(Objects.nonNull(commentTasks) && !commentTasks.isEmpty()){
            commentTasks.stream().forEach(o-> {
                CommentTaskDTO commentTaskDTO = new CommentTaskDTO();
                BeanUtils.copyProperties(o, commentTaskDTO);
                Users u = usersService.findById(o.getUserId());
                commentTaskDTO.setUserId(u);
                commentTaskDTO.setTaskId(o.getTask().getTaskId());
                if (Objects.nonNull(o.getFile())) {
                    List<String> listFile = new ArrayList<>();
                    if (o.getFile().contains(";")) {
                        List<String> items = Arrays.asList(o.getFile().split(";"));
                        items.stream().forEach(i->{
                            listFile.add(minioUtils.getFileUrl(i));
                        });
                    } else {
                        listFile.add(minioUtils.getFileUrl(o.getFile()));
                    }
                    commentTaskDTO.setFiles(listFile);
                }
                commentTaskDTOList.add(commentTaskDTO);
            });
        }
        return commentTaskDTOList;
    }

}
