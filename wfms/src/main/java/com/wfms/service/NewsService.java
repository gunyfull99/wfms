package com.wfms.service;

import com.wfms.entity.News;

import java.util.List;

public interface NewsService {
    Long getTotalNotifiNotSeen(Long userId);
    List<News> getListNews(Long userId);
    List<News> getListNewsNotSeen(Long userId);
    News updateNewsSeen(Long newsId);
}
