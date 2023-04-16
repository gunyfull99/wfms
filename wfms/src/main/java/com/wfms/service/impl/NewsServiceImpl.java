package com.wfms.service.impl;

import com.wfms.entity.News;
import com.wfms.repository.NewsRepository;
import com.wfms.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Override
    public Long getTotalNotifiNotSeen(Long userId) {
        return newsRepository.getTotalNotificationWithStatus(userId);
    }

    @Override
    public List<News> getListNews(Long userId) {
        return newsRepository.getNotificationByUserId(userId);
    }

    @Override
    public List<News> getListNewsNotSeen(Long userId) {
        return newsRepository.getNotificationByUserIdAndStatus(userId,1L);
    }

    @Override
    public News updateNewsSeen(Long newsId) {
        newsRepository.updateStatusByNewsId(newsId);
        return newsRepository.findById(newsId).get();
    }
}
