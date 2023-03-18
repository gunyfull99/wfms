package com.wfms.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfms.Dto.CommentIssueDTO;
import com.wfms.entity.CommentIssue;
import com.wfms.entity.Issue;
import com.wfms.entity.Users;
import com.wfms.repository.CommentIssueRepository;
import com.wfms.repository.IssueRepository;
import com.wfms.service.CommentIssueService;
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
public class CommentIssueServiceImpl implements CommentIssueService {
    @Autowired
    private CommentIssueRepository commentIssueRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private MinioUtils minioUtils;
    @Override
    public List<CommentIssue> findAll() {
        return commentIssueRepository.findAll();
    }

    @Override
    public Page<CommentIssue> findWithPage(int page, int total) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return commentIssueRepository.findAll(pageable);
    }

    @Override
    public List<CommentIssue> findCommentIssueByIssueId(Long issueId) {
        return commentIssueRepository.findByIssueId(issueId);
    }

    @Override
    public CommentIssueDTO createComment(String comment, List<MultipartFile> images) {
        CommentIssueDTO commentIssueDTO=new CommentIssueDTO();
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            commentIssueDTO =objectMapper.readValue(comment,CommentIssueDTO.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        Assert.notNull(commentIssueDTO.getContent(),"Content must not be null");
        Assert.notNull(commentIssueDTO.getIssueId(),"IssueId must not be null");
        Assert.notNull(commentIssueDTO.getUserId(),"UserId() must not be null");
        Issue issueData = issueRepository.getById(commentIssueDTO.getIssueId());
        Assert.notNull(issueData,"Not found IssueId "+commentIssueDTO.getIssueId());
        Users users =usersService.findById(commentIssueDTO.getUserId().getId());
        Assert.notNull(users,"Not found userId "+commentIssueDTO.getUserId().getId());
        String filenames ="" ;
        String filename =null ;
        CommentIssue commentIssue= new CommentIssue();
        BeanUtils.copyProperties(commentIssueDTO,commentIssue);
        if(Objects.nonNull(images) && !images.isEmpty()){
            for (int i = 0; i <images.size() ; i++) {
                filename= StringUtils.cleanPath((Objects.requireNonNull(images.get(i).getOriginalFilename())));
                filename = filename.substring(0, filename.lastIndexOf("."))
                        .replace(".", "").replace(";","-") + "." + filename.substring(filename.lastIndexOf(".") + 1);

                filename=minioUtils.uploadFile(filename,images.get(i));
                filenames += (i!=images.size()-1 ? filename+";" : filename);
            }
            commentIssue.setFile(filenames);
        }
        commentIssue.setCommentIssueId(null);
        commentIssue.setCreateDate(new Date());
        commentIssue.setStatus(1);
        commentIssue.setIssue(Issue.builder().issueId(commentIssueDTO.getIssueId()).build());
        commentIssue.setUserId(commentIssueDTO.getUserId().getId());
        commentIssue=commentIssueRepository.save(commentIssue);
        BeanUtils.copyProperties(commentIssue,commentIssueDTO);

        return commentIssueDTO;
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
    public List<CommentIssueDTO> getCommentByIssue(Long issueId) {
        Assert.notNull(issueId,"IssueId must not be null");
        Issue issueData = issueRepository.getById(issueId);
        Assert.notNull(issueData,"Not found IssueId "+issueId);
        List<CommentIssue> commentIssues= commentIssueRepository.findByIssueId(issueId);
        List<CommentIssueDTO> commentIssueDTOList=new ArrayList<>();
        if(Objects.nonNull(commentIssues) && !commentIssues.isEmpty()){
            for (int i = 0; i <commentIssues.size() ; i++) {
                CommentIssueDTO commentIssueDTO=new CommentIssueDTO();
                BeanUtils.copyProperties(commentIssues.get(i),commentIssueDTO);
                Users u =usersService.findById(commentIssues.get(i).getUserId());
                commentIssueDTO.setUserId(u);
                commentIssueDTO.setIssueId(commentIssues.get(i).getIssue().getIssueId());
                if(Objects.nonNull(commentIssues.get(i).getFile())){
                    List<String> listFile = new ArrayList<>();
                    if(commentIssues.get(i).getFile().contains(";")){
                        List<String> items = Arrays.asList(commentIssues.get(i).getFile().split(";"));
                            for (int j = 0; j <items.size() ; j++) {
                                listFile.add(minioUtils.getFileUrl(items.get(i)));
                            }
                    }else{
                        listFile.add(minioUtils.getFileUrl(commentIssues.get(i).getFile()));
                    }
                    commentIssueDTO.setFiles(listFile);
                }
                commentIssueDTOList.add(commentIssueDTO);
            }
        }
        return commentIssueDTOList;
    }

}
