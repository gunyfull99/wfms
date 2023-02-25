package com.wfms.service.impl;

import com.wfms.Dto.SprintDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.config.Const;
import com.wfms.entity.Projects;
import com.wfms.entity.Sprint;
import com.wfms.entity.Task;
import com.wfms.repository.TaskRepository;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.SprintRepository;
import com.wfms.service.SprintService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SprintServiceImpl implements SprintService {
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }
    @Override
    public Page<Sprint> findAllWithPage(int total, int page) {
        Pageable pageable = PageRequest.of(page,total, Sort.Direction.DESC);
        return sprintRepository.findAll(pageable);
    }

    @Override
    public List<SprintDTO> findSprintByProjectId(Long projectId) {
        List<Sprint> list=sprintRepository.findSprintByProjectId(projectId);
        return getListSprintDto(list);
    }
    public  List<SprintDTO> getListSprintDto( List<Sprint> list){
        List<SprintDTO> list1 = new ArrayList<>();
        if(Objects.nonNull(list) && !list.isEmpty()){
            for (Sprint s: list
            ) {
                SprintDTO sprintDTO= new SprintDTO();
                BeanUtils.copyProperties(s,sprintDTO);
                sprintDTO.setProjectId(s.getProjects().getProjectId());
                list1.add(sprintDTO);
            }
        }
        return list1;
    }
    @Override
    public ObjectPaging searchSprint(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("sprintId").descending());
        Page<Sprint> list =sprintRepository.findSprintByProjectId(objectPaging.getProjectId(), objectPaging.getStatus(), Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,pageable);
        List<SprintDTO> sprintDTOList=getListSprintDto(list.getContent());
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(sprintDTOList).build();
    }

    @Override
    public SprintDTO createSprint(SprintDTO sprintDTO) {
        Assert.isTrue(Objects.nonNull(sprintDTO.getSprintName()) && !sprintDTO.getSprintName().trim().equals(""),"SprintName must not be null");
        Assert.isTrue(Objects.nonNull(sprintDTO.getGoal()) && !sprintDTO.getGoal().trim().equals(""),"Goal must not be null");
        Assert.isTrue(Objects.nonNull(sprintDTO.getStartDate()),"StartDate must not be null");
        Assert.isTrue(sprintDTO.getStartDate().isAfter(LocalDateTime.now()),"StartDate must after now");
        Assert.isTrue(Objects.nonNull(sprintDTO.getEndDate()),"EndDate must not be null");
        Assert.isTrue(Objects.nonNull(sprintDTO.getProjectId()), Const.responseError.projectId_null);
        Projects p = projectRepository.findById(sprintDTO.getProjectId()).get();
        Assert.notNull(p,Const.responseError.project_notFound+ sprintDTO.getProjectId());
        List<Sprint> s = sprintRepository.getSprintByName(sprintDTO.getSprintName().toLowerCase(),p.getProjectId());
        Assert.isTrue(!DataUtils.listNotNullOrEmpty(s),"Sprint name is exsist in this project");
        if(p.getStatus()==2){
            Assert.isTrue(false,"Project closed");
        }else if(p.getStatus()==1){
            Assert.isTrue(false,"Project not start");
        }else if(p.getStatus()==0){
            Assert.isTrue(false,"Project inactive");
        }
        Sprint sprint = new Sprint();
        BeanUtils.copyProperties(sprintDTO,sprint);
        sprint.setStatus(1);
        sprint.setProjects(Projects.builder().projectId(sprintDTO.getProjectId()).build());
        sprint.setCreateDate(LocalDateTime.now());
        sprintRepository.save(sprint);
        BeanUtils.copyProperties(sprint,sprintDTO);
        return sprintDTO;
    }

    @Override
    public SprintDTO getDetailSprint(Long sprintId) {
        Assert.notNull(sprintId,Const.responseError.sprintId_null);
        Sprint sprint = sprintRepository.getDetailSprintById(sprintId);
        Assert.isTrue(Objects.nonNull(sprint),Const.responseError.sprint_notFound+sprintId);
        SprintDTO sprintDTO= new SprintDTO();
        BeanUtils.copyProperties(sprint,sprintDTO);
        sprintDTO.setProjectId(sprint.getProjects().getProjectId());
        return sprintDTO;
    }

    @Override
    public Sprint updateSprint(Sprint sprintDTO) {
        Assert.isTrue(Objects.nonNull(sprintDTO.getSprintId()),Const.responseError.sprintId_null);
//        Assert.notNull(sprintDTO.getProjects(),"ID dự án không được để trống");
//        Assert.notNull(sprintDTO.getProjects().getProjectId(),"ID dự án không được để trống");
    //    Projects projects = projectRepository.getById(sprintDTO.getProjects().getProjectId());
   //     Assert.notNull(projects,"Không tìm thấy project với ID "+sprintDTO.getProjects().getProjectId());
        Sprint s = sprintRepository.findById(sprintDTO.getSprintId()).get();
        Assert.notNull(s,Const.responseError.sprint_notFound+sprintDTO.getSprintId());
        Sprint sprint = new Sprint();
        BeanUtils.copyProperties(sprintDTO,sprint);
        sprint.setProjects(s.getProjects());
        sprint.setUpdateDate(LocalDateTime.now());
        return  sprintRepository.save(sprint);
    }

    @Override
    public String completeSprint(Long sprintId) {
        Assert.notNull(sprintId,Const.responseError.sprintId_null);
        Sprint sprint = sprintRepository.getDetailSprintById(sprintId);
        Assert.notNull(sprint,Const.responseError.sprint_notFound+sprintId);
        Projects p = projectRepository.findById(sprint.getProjects().getProjectId()).get();
        Assert.notNull(p,Const.responseError.project_notFound+ sprint.getProjects().getProjectId());
        if(p.getStatus()==2){
            Assert.isTrue(false,"Project closed");
        }else if(p.getStatus()==1){
            Assert.isTrue(false,"Project not start");
        }else if(p.getStatus()==0){
            Assert.isTrue(false,"Project inactive");
        }
        Assert.isTrue(sprint.getStatus()==3,"Sprint status must active");
        List<Task> taskList = taskRepository.getListTaskInSprintAndClose(sprintId);
        List<String>tasks=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(taskList)){
            tasks=taskList.stream().map(Task::getCode).collect(Collectors.toList());
        }
        Assert.isTrue(taskList.isEmpty(),"Have "+tasks.size()+" task not complete ");
        sprint.setStatus(2);
        sprintRepository.save(sprint);
        return "Complete sprint successfull!";
    }

    @Override
    public String startSprint(Long sprintId) {
        Assert.notNull(sprintId,Const.responseError.sprintId_null);
        Sprint sprint = sprintRepository.getDetailSprintById(sprintId);
        Assert.notNull(sprint,Const.responseError.sprint_notFound+sprintId);
        Projects p = projectRepository.findById(sprint.getProjects().getProjectId()).get();
        Assert.notNull(p,Const.responseError.project_notFound+ sprint.getProjects().getProjectId());
        if(p.getStatus()==2){
            Assert.isTrue(false,"Project closed");
        }else if(p.getStatus()==1){
            Assert.isTrue(false,"Project not start");
        }else if(p.getStatus()==0){
            Assert.isTrue(false,"Project inactive");
        }
        Assert.isTrue(sprint.getStatus()==1,"Sprint status must not start");
      //  List<Sprint> list= sprintRepository.findSprintByProjectIdAndClose(p.getProjectId());
       // Assert.isTrue(list.isEmpty(),"Hãy hoàn thành sprint cũ trước khi bắt đầu sprint mới");
        sprint.setStatus(3);
        sprintRepository.save(sprint);
        return "Start sprint successfull!";
    }
}
