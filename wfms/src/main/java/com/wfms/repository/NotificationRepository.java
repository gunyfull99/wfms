package com.wfms.repository;

import com.wfms.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query(value = "Select count(*) from notification n where n.status = 1 and n.user_id = :userId",nativeQuery = true)
    Long getTotalNotificationWithStatus(@Param("userId") Long userId);
    @Query(value = "Select * from notification n where n.user_id = :userId",nativeQuery = true)
    List<Notification> getNotificationByUserId(@Param("userId") Long userId);
    @Query(value = "Select * from notification n where n.user_id = :userId and status = :status",nativeQuery = true)
    List<Notification> getNotificationByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    @Modifying
    @Transactional
    @Query(value = " Update notification  set status = 0 where notification_id = :notificationId and status = 1",nativeQuery = true)
    void updateStatusByNotificationId(@Param("notificationId") Long notificationId);

    @Query(value = "Select p from Notification p where  " +
            " userId = :userId " +
            "and (:status is null OR (p.status) = :status) " +
            "and (:keyword is null OR LOWER(p.content) LIKE %:keyword% " +
            "or LOWER(p.description) LIKE %:keyword% " +
            "or LOWER(p.title) LIKE %:keyword% " +
            ") ")
    Page<Notification> getProjectsByMember(@Param("userId") Long userId,@Param("status") Integer status, @Param("keyword") String keyword, Pageable pageable);
}
