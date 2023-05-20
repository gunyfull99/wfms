package com.wfms.UserTest;

import com.wfms.ConfigTest;
import com.wfms.Dto.CreateUsersDto;
import com.wfms.Dto.JwtRequest;
import com.wfms.Dto.UsersDto;
import com.wfms.entity.Users;
import com.wfms.repository.UsersRepository;
import com.wfms.service.UsersService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


 import java.time.LocalDateTime; 
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class UserUnitTest extends ConfigTest {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository usersRepository;
    @Before
    @Override
    public void setUp(){
        super.setUp();
    }
    // viết nghịch cho vui
//    @Test
//    public void testFoundUser(){
//        Users users = usersService.getByUsername("trungdv");
//        Assert.assertNotNull("Found user success", users);
//    }
//    @Test
//    public void testGetAllUser(){
//        List<Users> users = usersService.listAllUsers();
//        Assert.assertNotNull("Found all user success", users);
//    }
    // Thực hiện unit test từ API
    /*
    * 1. get user by id
    * 2. get user detail
    * 3. login
    * 4. create user
    * 5. update user*/
    @Test
    public void loginTest() throws Exception {
        JwtRequest jwtRequest = JwtRequest.builder().username("1111").password("1111").build();
        String url = "/users/login";
        String inputJson = super.mapToJson(jwtRequest);
        System.out.println(inputJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull("Login fail pass",content);

    }
    @Test
    public void getUserDetail() throws Exception {
        Users users = usersRepository.findAll().get(0);
        String url = "/users/";
        url = url.concat(String.valueOf(users.getId()));
        System.out.println(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(400, status);
        Assert.assertNotNull("Get user detail success",content);
    }
    @Test
    public void createUser() throws Exception {
        String url = "/users";
        Random generator = new Random();
        String userName = String.valueOf(generator.nextInt());
        CreateUsersDto createUsersDto = CreateUsersDto.builder()
                .username(userName)
                .password("123")
                .fullName(userName)
                .emailAddress("test@gmail.com")
                .jobTitle("DEV")
                .gender(1)
                .phone("098177261")
                .roles(1L)
                .address("Hải Phòng")
                .birthDay(LocalDateTime.now())
                .build();
        String inputJson = super.mapToJson(createUsersDto);
        System.out.println(inputJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(200, status);
        Assert.assertNotNull("create user success",content);
    }
    @Test
    public void updateUser() throws Exception {
        String url = "/users";
        Users users = usersRepository.findAll().get(0);
        UsersDto usersDto = new UsersDto();
        BeanUtils.copyProperties(users,usersDto);
        String inputJson = super.mapToJson(usersDto);
        System.out.println(inputJson);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
        assertEquals(200, status);
        Assert.assertNotNull("create user success",content);
    }
}
