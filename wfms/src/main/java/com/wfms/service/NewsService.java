package com.wfms.service;

import com.wfms.entity.News;

import java.util.List;

public interface NewsService {
    Long getTotalNotifiNotSeen(String token);
    List<News> getListNews(String token);
    List<News> getListNewsNotSeen(String token);
    List<News> getListNewsSeen(String token);
    News updateNewsSeen(Long newsId);
}
