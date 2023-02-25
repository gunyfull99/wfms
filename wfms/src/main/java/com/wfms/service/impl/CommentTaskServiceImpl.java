package com.wfms.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.CommentTaskDTO;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.config.Const;
import com.wfms.entity.CommentTask;
import com.wfms.entity.Notification;
import com.wfms.entity.Task;
import com.wfms.entity.Users;
import com.wfms.repository.CommentTaskRepository;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.TaskRepository;
import com.wfms.repository.TaskUsersRepository;
import com.wfms.service.CommentTaskService;
import com.wfms.service.FireBaseService;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
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


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private TaskUsersRepository taskUsersRepository;
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
    public CommentTaskDTO createComment(String comment, List<MultipartFile> images) throws FirebaseMessagingException {
        CommentTaskDTO commentTaskDTO =new CommentTaskDTO();
        System.out.println("76zzzz "+comment);
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            commentTaskDTO =objectMapper.readValue(comment, CommentTaskDTO.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        System.out.println("84zzzz "+commentTaskDTO.getUserId());
        Assert.notNull(commentTaskDTO.getContent(), Const.responseError.content_null);
        Assert.notNull(commentTaskDTO.getTaskId(),Const.responseError.taskId_null);
        Assert.notNull(commentTaskDTO.getUserId(),Const.responseError.userId_null);
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
        commentTask.setCreateDate(LocalDateTime.now());
        commentTask.setStatus(1);
        commentTask.setTask(com.wfms.entity.Task.builder().taskId(commentTaskDTO.getTaskId()).build());
        commentTask.setUserId(commentTaskDTO.getUserId().getId());
        commentTask = commentTaskRepository.save(commentTask);
        BeanUtils.copyProperties(commentTask, commentTaskDTO);
        List<Notification> notificationEntities =new ArrayList<>();
        List<Long> u= taskUsersRepository.findUserInTask(commentTaskDTO.getTaskId())
                .stream().filter(o-> !Objects.equals(o, users.getId())).collect(Collectors.toList());
        System.out.println("115zzzz "+u);
        if(DataUtils.listNotNullOrEmpty(u)){
            String content = commentTaskDTO.getContent();
            String[] split = content.split(" ");
            if(split.length>8){
                content=split[0]+" "+split[1]+" "+split[2]+" "+split[3]+" "+split[4]+" "+split[5]+" "+split[6]+" "+split[7]+" ...";
            }
            String finalContent = content;
            System.out.println("123zzzz "+u);
            u.forEach(o->{
                System.out.println("124zzzz "+o);
                notificationEntities.add(Notification.builder()
                        .taskId(taskData.getTaskId())
                        .userId(o)
                        .title(users.getFullName()+" comment to task "+taskData.getCode())
                        .description(finalContent)
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
            });
            MessageDto messageDtoList =   MessageDto.builder().userId(u)
                    .notification(NotificationDto.builder().taskId(taskData.getTaskId()).title(users.getFullName()+" comment to task "+taskData.getCode()).body(finalContent).build()).build();
            fireBaseService.sendManyNotification(messageDtoList);
            System.out.println("139zzzz "+u);
            notificationRepository.saveAll(notificationEntities);
        }

        return commentTaskDTO;
    }

    @Override
    public CommentTaskDTO updateComment(String comment, String listImageWantDelete, List<MultipartFile> images) {
        CommentTaskDTO commentTaskDTO =new CommentTaskDTO();
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            commentTaskDTO =objectMapper.readValue(comment, CommentTaskDTO.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        Assert.notNull(commentTaskDTO.getContent(), Const.responseError.content_null);
        Assert.notNull(commentTaskDTO.getCommentTaskId(), "CommentTaskId must not be null");
       // CommentTaskDTO t = getDetailComment(commentTaskDTO.getCommentTaskId());
        String filename =null ;
        CommentTask commentTask = commentTaskRepository.findById(commentTaskDTO.getCommentTaskId()).get();
        Assert.notNull(commentTask, "Not found comment with id "+commentTaskDTO.getCommentTaskId());
        String filenames =commentTask.getFile() ;
        List<String> list = new ArrayList<>();
        if(Objects.nonNull(listImageWantDelete)){
            if(listImageWantDelete.contains(";")){
                list= List.of(listImageWantDelete.split(";"));
            }else{
                filenames=filenames.replace(listImageWantDelete,"");
            }
        }
        if(DataUtils.listNotNullOrEmpty(list)){
            for (String i :list) {
                filenames=filenames.replace(i,"");
            }
        }
        if(Objects.nonNull(images) && !images.isEmpty()){
            filenames+=";";
            for (int i = 0; i <images.size() ; i++) {
                filename= StringUtils.cleanPath((Objects.requireNonNull(images.get(i).getOriginalFilename())));
                    filename = filename.substring(0, filename.lastIndexOf("."))
                            .replace(".", "").replace(";","-") + "." + filename.substring(filename.lastIndexOf(".") + 1);
                    filename=minioUtils.uploadFile(filename,images.get(i));
                    filenames += (i!=images.size()-1 ? filename+";" : filename);
            }
        }
        commentTask.setFile(filenames);
        commentTask.setContent(commentTaskDTO.getContent());
        commentTask.setUpdateDate(LocalDateTime.now());
        commentTask.setType(commentTaskDTO.getType());
        commentTaskRepository.save(commentTask);
        return commentTaskDTO;
    }

    @Override
    public CommentTaskDTO getDetailComment(Long commentTaskId) {
        Assert.notNull(commentTaskId, "CommentTaskId must not be null");
        CommentTask t = commentTaskRepository.findById(commentTaskId).get();
        Assert.notNull(t, "Not found comment with id "+commentTaskId);
        CommentTaskDTO commentTaskDTO = new CommentTaskDTO();
        BeanUtils.copyProperties(t, commentTaskDTO);
        Users u = usersService.findById(t.getUserId());
        commentTaskDTO.setUserId(u);
        commentTaskDTO.setTaskId(t.getTask().getTaskId());
        if (Objects.nonNull(t.getFile())) {
            List<String> listFile = new ArrayList<>();
            if (t.getFile().contains(";")) {
                List<String> items = Arrays.asList(t.getFile().split(";"));
                if (DataUtils.listNotNullOrEmpty(items)) {
                    items.forEach(i->{
                        listFile.add(minioUtils.getFileUrl(i));
                    });
                }

            } else {
                listFile.add(minioUtils.getFileUrl(t.getFile()));
            }
            commentTaskDTO.setFiles(listFile);
        }
        return commentTaskDTO;
    }

    @Override
    public String deleteComment(Long commentTaskId) {
        Assert.notNull(commentTaskId, "CommentTaskId must not be null");
        CommentTask t = commentTaskRepository.findById(commentTaskId).get();
        Assert.notNull(t, "Not found comment with id "+commentTaskId);
        commentTaskRepository.delete(t);
        return "Delete comment successful";
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
        Assert.notNull(taskId,Const.responseError.taskId_null);
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
                        if (DataUtils.listNotNullOrEmpty(items)) {
                            items.forEach(i->{
                                listFile.add(minioUtils.getFileUrl(i));
                            });
                        }

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
