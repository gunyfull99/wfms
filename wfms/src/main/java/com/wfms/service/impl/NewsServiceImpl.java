package com.wfms.service.impl;

import com.wfms.entity.News;
import com.wfms.entity.Users;
import com.wfms.repository.NewsRepository;
import com.wfms.service.NewsService;
import com.wfms.service.UsersService;
import com.wfms.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
    @Override
    public Long getTotalNotifiNotSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return newsRepository.getTotalNotificationWithStatus(users.getId());
    }

    @Override
    public List<News> getListNews(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return newsRepository.getNotificationByUserId(users.getId());
    }

    @Override
    public List<News> getListNewsNotSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return newsRepository.getNotificationByUserIdAndStatus(users.getId(),1);
    }

    @Override
    public List<News> getListNewsSeen(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        return newsRepository.getNotificationByUserIdAndStatus(users.getId(),0);
    }

    @Override
    public News updateNewsSeen(Long newsId) {
        newsRepository.updateStatusByNewsId(newsId);
        return newsRepository.findById(newsId).get();
    }
}
