package com.wfms.controller;
import com.wfms.Dto.*;
//import com.wfms.config.ResponseError;
import com.wfms.entity.Roles;
import com.wfms.entity.Users;
import com.wfms.exception.ResourceBadRequestException;
import com.wfms.exception.ResourceNotFoundException;
import com.wfms.service.MyUserDetailsService;
import com.wfms.service.UsersService;
import com.wfms.utils.JwtUtility;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UsersController {
//    @Autowired
//    private ResponseError r;
    @Autowired
    private HttpSession session;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService Userservice;
    // get detail Users
    // http://localhost:8091/Users/{id}
    @CrossOrigin(origins = "http://localhost:8091/users/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetailUser(@Valid @PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        try {
            Users Users = Userservice.findById(id);
            return ResponseEntity.ok().body(Userservice.getAccById(Users));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get detail Users
    // http://localhost:8091/Users/getuserbyusername/{name}
    @CrossOrigin(origins = "http://localhost:8091/users/getuserbyusername/{name}")
    @GetMapping("/getuserbyusername/{name}")
    public ResponseEntity<Object> getDetailUser(@Valid @PathVariable(name = "name") String name) throws ResourceNotFoundException {
        try {
            Users Users = Userservice.getByUsername(name);
            return ResponseEntity.ok().body(Userservice.getAccById(Users));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Login
    // http://localhost:8091/Users/login
    @PostMapping("/login")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws ResourceNotFoundException, ResourceBadRequestException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername().toLowerCase(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Sai mật khẩu"));
        }
        final UserDetails userDetails
                = myUserDetailsService.loadUserByUsername(jwtRequest.getUsername().toLowerCase());
        final String token =
                jwtUtility.generateToken(userDetails);
        UsersDto a = Userservice.getAccByUsername(jwtRequest.getUsername().toLowerCase());
        if (a.getStatus() == 0) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Tài khoản bị khóa"));
        }
        return new JwtResponse(token, a);
    }

    // Logout
    // http://localhost:8091/Users/logout
    @PostMapping("/logout")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Logout success", response = String.class)})
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Đăng xuất thành công!";
    }

    // Create Users
    // http://localhost:8091/Users
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PostMapping("")
    public ResponseEntity<BaseResponse> createUsers(@Valid @RequestBody CreateUsersDto a) throws ResourceBadRequestException {

        Users Users = Userservice.getByUsername(a.getUsername());
        if (Users != null) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Tài khoản đã tổn tại"));
        } else {
            return new ResponseEntity<BaseResponse>(Userservice.createUsers(a), HttpStatus.CREATED);
        }
    }

    // Update Users
    // http://localhost:8091/Users
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PutMapping("")
    public ResponseEntity<Object> updateUsers(@Valid @RequestBody UsersDto a)
            throws ResourceBadRequestException {
        try {
            Users UsersRequest = Userservice.getByUsername(a.getUsername());
            if (UsersRequest == null) {
                throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản"));
            }
            UsersRequest = Userservice.convertUsers(UsersRequest, a);
            Users Users = Userservice.save(UsersRequest);
            return ResponseEntity.ok().body(Userservice.updateUser(Users));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // admin change pass
    // http://localhost:8091/Users/admin/changepass
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PutMapping("/admin/changepass")
    public ResponseEntity<Object> adminChangePass(@Valid @RequestBody Users a)
            throws ResourceNotFoundException, ResourceBadRequestException {
        try {
            Users UsersRequest = Userservice.getByUsername(a.getUsername());
            if (UsersRequest == null) {
                throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy id"));
            }
            UsersRequest.setPassword(a.getPassword());
            UsersDto Users = Userservice.saveUserWithPassword(UsersRequest);
            return ResponseEntity.ok().body(Users);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // user change pass
    // http://localhost:8091/Users/changepass
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PutMapping("/changepass")
    public ResponseEntity<Object> userChangePass(@Valid @RequestBody ChangePassForm form)
            throws ResourceNotFoundException, ResourceBadRequestException {
        try {
            return ResponseEntity.ok().body( Userservice.UserChangePass(form));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // create role(ex:,ROLE_ADMIN,ROLE_USER,...)
    // http://localhost:8091/Users/role/save
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PostMapping("/role/save")
    public ResponseEntity<BaseResponse> createRole(@Valid @RequestBody Roles role) {
        return new ResponseEntity<BaseResponse>(Userservice.saveRole(role), HttpStatus.CREATED);
    }

    // add role to User
    // http://localhost:8091/Users/role/addtoUsers
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PostMapping("/role/addtoUsers")
    public ResponseEntity<Object> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        try {
            Userservice.addRoleToUser(form.getUsername(), form.getRoleId());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // delete role to User
    // http://localhost:8091/Users/role/deleteroleUsers
    @CrossOrigin(origins = "http://localhost:8091/users")
    @DeleteMapping("/role/deleteroleUsers")
    public ResponseEntity<Object> deleteRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        try {
            Userservice.removeRoleToUser(form.getUsername(), form.getRoleId());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // get role not in Users
    // http://localhost:8091/Users/list/notrole/2
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/list/notrole/{id}")
    public ResponseEntity<Object> getRoleNotInUser(@PathVariable(name = "id") long id) {
        try {
            return ResponseEntity.ok().body(Userservice.getUserNotRole(id));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // get role  in Users
    // http://localhost:8091/Users/list/haverole/2
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/list/haverole/{id}")
    public ResponseEntity<Object> getRoleHaveInUser(@PathVariable(name = "id") long id) {
        try {
            return ResponseEntity.ok().body(Userservice.getUserHaveRole(id));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // get all role
    // http://localhost:8091/Users/role/list
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/role/list")
    public ResponseEntity<Object> getAllRole() {
        try {
            return ResponseEntity.ok().body(Userservice.findAllRole());
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get all Users
    // http://localhost:8091/Users/list
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/list")
    public ResponseEntity<Object> getAllUsers() {
        try {
            //        Page<Users> Users = Userservice.findAllProject(UsersPaging);
//        List<UsersDto> list = Userservice.convertUsers(Users.getContent());
            return ResponseEntity.ok().body(Userservice.listAllUsers());
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // block list user
    // http://localhost:8091/Users/blockusers
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PutMapping("/blockusers")
    public ResponseEntity<Object> blockUsers(@RequestBody List<Long> listUser) {
        try {
            Userservice.blockListUser(listUser);
            return ResponseEntity.ok().body("Block success");
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // search user with paging
    // http://localhost:8091/Users/searchWithPaging
    @CrossOrigin(origins = "http://localhost:8091/users")
    @PostMapping("/searchWithPaging")
    public ResponseEntity<Object> searchUserWithPaging(@RequestBody UsersPaging UsersPaging) {
        try {
            Page<Users> list = Userservice.searchUserWithPaging(UsersPaging);
            List<UsersDto> list1 = Userservice.convertUsers(list.getContent());
            return ResponseEntity.ok().body(new UsersPaging((int) list.getTotalElements(),
                    list1, UsersPaging.getPage(), UsersPaging.getLimit(), UsersPaging.getSearch(), UsersPaging.getRole(), UsersPaging.getUserType()));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // search user
    // http://localhost:8091/Users/search/{name}
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/search/{name}")
    public ResponseEntity<Object> searchUser(@PathVariable(name = "name") String name) {
        try {
            return ResponseEntity.ok().body(Userservice.searchUser(name));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get list user id
    // http://localhost:8091/Users/listuserid/{name}
    @CrossOrigin(origins = "http://localhost:8091/users")
    @GetMapping("/listuserid/{name}")
    public ResponseEntity<Object> getListUserId(@PathVariable(name = "name") String name) {
        try {
            return ResponseEntity.ok().body(Userservice.getListUserId(name));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // gửi mail đổi mật khẩu
    // http://localhost:8091/Users/sendmailpassword
    @CrossOrigin(origins = "http://localhost:8091/Users")
    @PostMapping("/sendmailpassword")
    public ResponseEntity<BaseResponse> sendMailPassword(@RequestBody ClientSdi sdi) {
        return ResponseEntity.ok().body(Userservice.sendMailPassWord(sdi));
    }
}