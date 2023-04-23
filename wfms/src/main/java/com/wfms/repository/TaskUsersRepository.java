package com.wfms.repository;

import com.wfms.entity.TaskUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskUsersRepository extends JpaRepository<TaskUsers, Long> {
    @Query(value = "select * from task_users where user_id = :userId and task_id = :taskId",nativeQuery = true)
    TaskUsers findTaskUsersByUserIdAndTaskId(@Param("userId")Long userId, @Param("taskId")Long taskId);
    @Query(value = "select * from task_users where task_id = :taskId and is_responsible= :isResponsible and status = 2",nativeQuery = true)
    TaskUsers findTaskUsersByTaskIdAndIsResponsible(@Param("taskId")Long userId, @Param("isResponsible")Boolean isResponsible);
    @Query(value = "select * from task_users where task_id = :taskId and status IN (1,2) ",nativeQuery = true)
    List<TaskUsers> findTaskUsersByTaskId(@Param("taskId")Long taskId);
    @Query(value = "select * from task_users where task_id = :taskId and status = 1",nativeQuery = true)
    List<TaskUsers> findTaskUsersRequestByTaskId(@Param("taskId")Long taskId);
}
