package com.wfms.ProjectTest;

import com.wfms.ConfigTest;
import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.ProjectUserDTO;
import com.wfms.Dto.UsersDto;
import com.wfms.entity.Projects;
import com.wfms.entity.Roles;
import com.wfms.entity.Users;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.UsersRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ProjectTest extends ConfigTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void createProject() throws Exception {
        List<Users> users = usersRepository.findAll();
        UsersDto usersDto = new UsersDto();
        List<Users> userPm = new ArrayList<>();
        List<Users> userMember = new ArrayList<>();
        users.forEach(i->{
            List<String> role= i.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
            if(role.contains("PM")){
                userPm.add(i);
            }else {
                userMember.add(i);
            }
        });
        BeanUtils.copyProperties(userPm.get(0),usersDto);
        List<UsersDto> usersDtos = new ArrayList<>();
        userMember.forEach(i-> {
            UsersDto usersD = new UsersDto();
            BeanUtils.copyProperties(i,usersD);
            usersDtos.add(usersD);
        });
        String url = "/project/create-project";
        System.out.println(url);
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("project test")
                .description("unit test")
                .shortName("test")
                .PriorityId(1L)
                .lead(usersDto)
                .userId(usersDtos)
                .build();
        String inputJson = super.mapToJson(projectDTO);
        System.out.println(inputJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull("created new project successsfully",content);
    }
    @Test
    public void updateProject() throws Exception {
        List<Projects> projects = projectRepository.findAll();
        Projects project = projects.get(projects.size()-1);
        project.setDescription("unit test for update project");
        String url = "/project/update-project";
        System.out.println(url);
        String inputJson = super.mapToJson(project);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(200, status);
        Assert.assertNotNull("update project with id "+project.getProjectId()+" successsfully",content);
    }
    @Test
    public void getProjectByLeader(){
        String url = "/project/list-by-lead";

        System.out.println(url);


    }
    @Test
    public void getListProjectByMember(){
        String url = "/project/list-by-member";
        System.out.println(url);
    }
    @Test
    public void addMemberFromProject() throws Exception {
        String url = "/project/add-user-to-project";
        System.out.println(url);
        List<Projects> projects = projectRepository.findAll();
        Projects project = projects.get(projects.size()-1);
        project.setDescription("unit test for update project");
        ProjectUserDTO projectUserDTO = ProjectUserDTO.builder().projectId(project.getProjectId()).userId(List.of(1L,2L)).build();
        String inputJson = super.mapToJson(projectUserDTO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(200, status);
        Assert.assertNotNull("Add member successsfully",content);
    }
    @Test
    public void removeMemberFromProject() throws Exception {
        String url = "/project/remove-user-in-project";
        System.out.println(url);
        List<Projects> projects = projectRepository.findAll();
        Projects project = projects.get(projects.size()-1);
        project.setDescription("unit test for update project");
        ProjectUserDTO projectUserDTO = ProjectUserDTO.builder().projectId(project.getProjectId()).userId(List.of(1L,2L)).build();
        String inputJson = super.mapToJson(projectUserDTO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(200, status);
        Assert.assertNotNull("remove member successsfully",content);
        System.out.println(url);
    }
}
