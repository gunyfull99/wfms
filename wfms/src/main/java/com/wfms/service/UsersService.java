package com.wfms.service;


import com.wfms.Dto.*;
import com.wfms.entity.Roles;
import com.wfms.entity.Users;
import com.wfms.exception.ResourceBadRequestException;
import com.wfms.exception.ResourceNotFoundException;
import com.wfms.repository.CompanyRepository;
import com.wfms.repository.RoleRepository;
import com.wfms.repository.UsersRepository;
import com.wfms.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;
//
//    @Autowired
//    private UsersRolesRepository usersRolesRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);


    public Users save(Users entity) {
        logger.info("save info of Users {}", entity.getFullName());

        return usersRepository.save(entity);
    }

    public List<Users> listAllUsers() {

        logger.info("find all Users");

        return usersRepository.findAll();
    }


    public void blockListUser(List<Long> listUser) {
        logger.info("Block list user");

        for (int i = 0; i < listUser.size(); i++) {
            Users a = usersRepository.selectById(listUser.get(i));
            a.setStatus(0);
            usersRepository.save(a);
        }
    }

    public Page<Users> findAll(UsersPaging ap) {
        int offset = ap.getPage();
        if (offset < 0) {
            offset = 1;
        }
        logger.info("Get all Users");
        Page<Users> a = usersRepository.findAll(PageRequest.of(offset - 1, ap.getLimit()));
        if (a.isEmpty()) {
            logger.error("no Users exist !!!");
            throw new RuntimeException("no Users exist !!!");
        }
        return a;
    }

    public List<UsersDto> convertUsers(List<Users> list) {
        List<UsersDto> a1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UsersDto aDto = updateUser(list.get(i));
            a1.add(aDto);
        }
        return a1;
    }

    public List<UsersDto> searchUser(String name) {
        logger.info("search user");
        List<Users> list = null;
        if (name == null || name.trim().equals("")) {
            list = usersRepository.findAll();
        } else {
            list = usersRepository.searchUser(name);
        }
        List<UsersDto> a1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UsersDto aDto = updateUser(list.get(i));
            a1.add(aDto);
        }
        return a1;
    }

    public List<Long> getListUserId(String name) {
        logger.info("list user id by name");

        List<Long> list = usersRepository.getListUserId(name);
        return list;
    }

    public List<Roles> findAllRole() {

        logger.info("get all role");
        return roleRepository.findAll();
    }


    public Users findById(Long aLong) {
        logger.info("get Users by id");
        return usersRepository.selectById(aLong);
    }

    public Users getById(Long id) {
        logger.info("get Users by id");
        Assert.notNull(id,"User id must not be null");
        return usersRepository.getById(id);
    }

    public UsersDto saveUserWithPassword(Users a) {
        logger.info("save user {}", a.getFullName());

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        Users acc = usersRepository.save(a);
        ModelMapper mapper = new ModelMapper();
        UsersDto accd = mapper.map(a, UsersDto.class);
        return accd;
    }

    public String createUsers(CreateUsersDto a) {
        logger.info("save user {}", a.getFullName());
        Assert.notNull(a.getEmailAddress(),"Email must not be null");
        Assert.notNull(a.getPassword(),"Password must not be null");
        Assert.notNull(a.getRoles(),"Role must not be null");
        Assert.notNull(a.getFullName(),"FullName must not be null");
        Assert.notNull(a.getBirthDay(),"BirthDay must not be null");
        Assert.notNull(a.getGender(),"Gender must not be null");
        Assert.notNull(a.getAddress(),"Address must not be null");
        Assert.isTrue(a.getBirthDay().getTime() < System.currentTimeMillis(),"BirthDay invalid");
        Set<Roles> roles = new HashSet<>();
        Roles r = roleRepository.findById(a.getRoles()).get();
        roles.add(r);
        a.setPassword(passwordEncoder.encode(a.getPassword()));
        ModelMapper mapper = new ModelMapper();
        Users acc =  mapper.map(a, Users.class);
        acc.setCompany(companyRepository.findComPanyById(a.getCompany()));
        acc.setRoles(roles);
        acc.setJobTitle(r.getName());
        acc.setStatus(1);
        acc.setCreatedDate(new Date());
        acc.setUsername(a.getUsername().toLowerCase());
        acc = usersRepository.save(acc);

        return  "Tạo tài khoản thành công";
    }

    public Users convertUsers(Users acc, UsersDto a) {
        acc.setPhone(a.getPhone());
        acc.setAddress(a.getAddress());
        acc.setBirthDay(a.getBirthDay());
        acc.setFullName(a.getFullName());
        acc.setJobTitle(a.getJobTitle());
        acc.setGender(a.getGender());
        acc.setEmailAddress(a.getEmailAddress());
        return acc;
    }

    public UsersDto updateUser(Users a) {
        logger.info("update user {}", a.getFullName());
        ModelMapper mapper = new ModelMapper();
        UsersDto acc = mapper.map(a, UsersDto.class);
        return acc;
    }

    public Users UserChangePass(ChangePassForm form) {
        logger.info("change password for user {}", form.getUsername());
        Users user = usersRepository.findByUsername(form.getUsername());
        Assert.notNull(user,"user not exist !!!");
        boolean match = passwordEncoder.matches(form.getOldPass(), user.getPassword());
        Assert.isTrue(match,"Wrong old password");
        Assert.isTrue(form.getNewPass().equals(form.getReNewPass()),"Re-NewPass not equal new pass");
            user.setPassword(passwordEncoder.encode(form.getNewPass()));
        return usersRepository.save(user);
    }


    public String saveRole(Roles role) {
        Assert.notNull(role,"Role must not be null");
        Assert.notNull(role.getId(),"RoleId must not be null");
        Assert.notNull(role.getName(),"Role name must not be null");
        logger.info("receive info to save for role {}", role.getName());
        Roles roles = roleRepository.save(role);
        return  "Create role " + role.getName() + " successful";
    }


    public Users getByUsername(String username) {
        logger.info("get Users By Username {}", username);
        Assert.notNull(username,"Username must not be null");
        return usersRepository.findByUsername(username);
    }


    public void addRoleToUser(String username, Long roleId) throws ResourceBadRequestException {
        logger.info("add Role To User {}", username);

        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản "));
        }

        Roles role = roleRepository.getById(roleId);
        if (role == null) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy role name "));
        }
        // usersRepository.addRole2User(user.getId(), role.getId());
        user.getRoles().add(role);
        user.setJobTitle(role.getName());
        usersRepository.save(user);

    }

    public void removeRoleToUser(String username, long roleId) throws ResourceBadRequestException {
        logger.info("remove Role To User {}", username);

        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản"));
        }
        Set<Roles> userRole = user.getRoles();
        user.getRoles().removeIf(x -> x.getId() == roleId);
        usersRepository.save(user);
    }



    public Set<Roles> getUserNotRole(Long id) {
        logger.info("get User Not Role");
        return roleRepository.getUserNotRole(id);
    }




    public List<Roles> getUserHaveRole(Long id) {

        logger.info("get User Have Role");
        return roleRepository.getUserHaveRole(id);
    }



    public UsersDto getAccByUsername(String username) {
        logger.info("get Users By Username {}", username);

        ModelMapper mapper = new ModelMapper();
        Users a = usersRepository.findByUsername(username);
        UsersDto acc = mapper.map(a, UsersDto.class);
        return acc;
    }

    public UsersDto getAccById(Users a) {
        logger.info("get Users By Id ");
        ModelMapper mapper = new ModelMapper();
        UsersDto acc = mapper.map(a, UsersDto.class);
        return acc;
    }

    public Page<Users> searchUserWithPaging(UsersPaging UsersPaging) {
        logger.info("Search user");
        Page<Users> a = null;
        Pageable pageable = PageRequest.of(UsersPaging.getPage() - 1, UsersPaging.getLimit(), Sort.by("id").descending());
        // a=usersRepository.filter(UsersPaging.getSearch(),Long.parseLong(UsersPaging.getRole()),UsersPaging.getUserType(),pageable);
//        if (UsersPaging.getRole() == null || UsersPaging.getRole().trim().equals("")) {
//            a = usersRepository.filterWhereNoRole(UsersPaging.getSearch(),
//                    UsersPaging.getUserType() == null || UsersPaging.getUserType().trim().equals("") ? "%%" : UsersPaging.getUserType(),
//                    pageable);
//        } else if (UsersPaging.getUserType() == null || UsersPaging.getUserType().trim().equals("")) {
//            a = usersRepository.findAllByFullNameContainingIgnoreCaseAndRolesIdAndStatus(UsersPaging.getSearch(),
//                    Long.parseLong(UsersPaging.getRole()), 1,
//                    pageable);
//        }
        a=usersRepository.findAllByFullNameOrEmailAddressContainingIgnoreCase(UsersPaging.getSearch(),UsersPaging.getSearch(),pageable);
        return a;
    }



    public String sendMailPassWord(ClientSdi sdi) throws ResourceNotFoundException {
        Assert.notNull(sdi,"Email must not be null" );
        Assert.notNull(sdi.getEmail(),"Email must not be null" );
        logger.info("Send mail");
        try {
            Users a = usersRepository.findByEmailAddress(sdi.getEmail());
            Assert.notNull(a,"Not found email " +sdi.getEmail());
            String newPass = DataUtils.generateTempPwd(9);
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(sdi.getEmail());
            dataMail.setSubject("Gửi lại mật khẩu ");
            Map<String, Object> props = new HashMap<>();
            props.put("password", newPass);
            dataMail.setProps(props);
            sendHtmlMail(dataMail, "client");
            a.setPassword(newPass);
            UsersDto Users = saveUserWithPassword(a);
            return "Send mail successful";
        } catch (MessagingException exp) {
            return "Send mail fail";
        }
    }

    public void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException {
        logger.info("Send mail");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        Context context = new Context();
        context.setVariables(dataMail.getProps());
        String html = templateEngine.process(templateName, context);
        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(html, true);
        mailSender.send(message);
    }

}
