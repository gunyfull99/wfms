package com.wfms.service;


import com.wfms.Dto.*;
//import com.wfms.config.ResponseError;
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
    private com.wfms.repository.UsersRepository UsersRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);


    public Users save(Users entity) {
        logger.info("save info of Users {}", entity.getFullName());

        return UsersRepository.save(entity);
    }

    public List<Users> listAllUsers() {

        logger.info("find all Users");

        return UsersRepository.findAll();
    }


    public void blockListUser(List<Long> listUser) {
        logger.info("Block list user");

        for (int i = 0; i < listUser.size(); i++) {
            Users a = UsersRepository.selectById(listUser.get(i));
            a.setStatus(0);
            UsersRepository.save(a);
        }
    }

    public Page<Users> findAll(UsersPaging ap) {
        int offset = ap.getPage();
        if (offset < 0) {
            offset = 1;
        }
        logger.info("Get all Users");
        Page<Users> a = UsersRepository.findAll(PageRequest.of(offset - 1, ap.getLimit()));
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
            list = UsersRepository.findAll();
        } else {
            list = UsersRepository.searchUser(name);
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

        List<Long> list = UsersRepository.getListUserId(name);
        return list;
    }

    public List<Roles> findAllRole() {

        logger.info("get all role");
        return roleRepository.findAll();
    }


    public Users findById(Long aLong) {
        logger.info("get Users by id");
        return UsersRepository.selectById(aLong);
    }

    public Users getById(Long id) {
        logger.info("get Users by id");
        return UsersRepository.getById(id);
    }

    public UsersDto saveUserWithPassword(Users a) {
        logger.info("save user {}", a.getFullName());

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        Users acc = UsersRepository.save(a);
        ModelMapper mapper = new ModelMapper();
        UsersDto accd = mapper.map(a, UsersDto.class);
        return accd;
    }

    public BaseResponse createUsers(CreateUsersDto a) {
        logger.info("save user {}", a.getFullName());
        if (a.getBirthDay().getTime() >= System.currentTimeMillis()) {
            return new BaseResponse(400, "Ngày sinh không hợp lệ ");
        }

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        Set<Roles> roles = new HashSet<>();
        roles.add(roleRepository.findById(a.getRoles()).get());
        ModelMapper mapper = new ModelMapper();
        Users acc = mapper.map(a, Users.class);
        acc.setCompany(companyRepository.findComPanyById(a.getCompany()));
        acc.setRoles(roles);
        acc.setStatus(1);
        acc.setUsername(a.getUsername().toLowerCase());

        acc = UsersRepository.save(acc);


        return new BaseResponse(200, "Tạo tài khoản thành công ");
    }

    public Users convertUsers(Users acc, UsersDto a) {
        acc.setPhone(a.getPhone());
        acc.setAddress(a.getAddress());
        acc.setBirthDay(a.getBirthDay());
        acc.setFullName(a.getFullName());
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

        Users user = UsersRepository.findByUsername(form.getUsername());

        if (user == null) {
            logger.error("user not exist !!!");
            throw new RuntimeException("user not exist !!!");
        }
        boolean match = passwordEncoder.matches(form.getOldPass(), user.getPassword());

        if (!match) {
            logger.error("Old pass  is wrong");

            throw new ResourceBadRequestException(new BaseResponse(400, "Sai mật khẩu cũ"));
        } else if (!form.getNewPass().equals(form.getReNewPass())) {
            logger.error("Re-NewPass not equal new pass");

            throw new ResourceBadRequestException(new BaseResponse(400, "2 mật khẩu không khớp"));
        } else {
            user.setPassword(passwordEncoder.encode(form.getNewPass()));
        }
        return UsersRepository.save(user);
    }


    public BaseResponse saveRole(Roles role) {
        logger.info("receive info to save for role {}", role.getName());
        Roles roles = roleRepository.save(role);
        return new BaseResponse(200, "Create role " + role.getName() + " successful");
    }


    public Users getByUsername(String username) {
        logger.info("get Users By Username {}", username);

        return UsersRepository.findByUsername(username);
    }


    public void addRoleToUser(String username, long roleId) throws ResourceBadRequestException {
        logger.info("add Role To User {}", username);

        Users user = UsersRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản "));
        }

        Roles role = roleRepository.getById(roleId);
        if (role == null) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy role name "));
        }
        // UsersRepository.addRole2User(user.getId(), role.getId());
        user.getRoles().add(role);
        UsersRepository.save(user);

    }

    public void removeRoleToUser(String username, long roleId) throws ResourceBadRequestException {
        logger.info("remove Role To User {}", username);

        Users user = UsersRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản"));
        }
        Set<Roles> userRole = user.getRoles();
        user.getRoles().removeIf(x -> x.getId() == roleId);
        UsersRepository.save(user);
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
        Users a = UsersRepository.findByUsername(username);
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

        // a=UsersRepository.filter(UsersPaging.getSearch(),Long.parseLong(UsersPaging.getRole()),UsersPaging.getUserType(),pageable);

        if (UsersPaging.getRole() == null || UsersPaging.getRole().trim().equals("")) {
            a = UsersRepository.filterWhereNoRole(UsersPaging.getSearch(),
                    UsersPaging.getUserType() == null || UsersPaging.getUserType().trim().equals("") ? "%%" : UsersPaging.getUserType(),
                    pageable);
        } else if (UsersPaging.getUserType() == null || UsersPaging.getUserType().trim().equals("")) {
            a = UsersRepository.findAllByFullNameContainingIgnoreCaseAndRolesIdAndStatus(UsersPaging.getSearch(),
                    Long.parseLong(UsersPaging.getRole()), 1,
                    pageable);
        }
        return a;
    }



    public BaseResponse sendMailPassWord(ClientSdi sdi) throws ResourceNotFoundException {
        logger.info("Send mail");
        try {
            Users a = UsersRepository.findByEmailAddress(sdi.getEmail());
            Assert.notNull(a,"Không tìm thấy user có mail:" +sdi.getEmail());
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
            return new BaseResponse(200, "Gửi mail thành công");
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
        return new BaseResponse(400, "Gửi mail thất bại");
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
