package com.wfms.repository;

import com.wfms.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    @Query(value = "Select count(*) from news n where n.status = 1 and n.user_id = :userId",nativeQuery = true)
    Long getTotalNotificationWithStatus(@Param("userId") Long userId);
    @Query(value = "Select * from news n where n.user_id = :userId",nativeQuery = true)
    List<News> getNotificationByUserId(@Param("userId") Long userId);
    @Query(value = "Select * from news n where n.user_id = :userId and status = :status",nativeQuery = true)
    List<News> getNotificationByUserIdAndStatus(@Param("userId") Long userId,@Param("status") Integer status);
    @Modifying
    @Transactional
    @Query(value = " Update news  set status = 0 where news_id = :newsId and status = 1",nativeQuery = true)
    void updateStatusByNewsId(@Param("newsId") Long newsId);
}
