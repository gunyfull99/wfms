package com.wfms.service;

import com.wfms.Dto.BaseResponse;
import com.wfms.entity.Users;
import com.wfms.exception.ResourceBadRequestException;
import com.wfms.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceBadRequestException {
        Users a = usersRepository.findByUsername(username);
        if (a == null) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy user"));
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            a.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(a.getUsername(), a.getPassword(),
                    authorities);
        }
    }


}
