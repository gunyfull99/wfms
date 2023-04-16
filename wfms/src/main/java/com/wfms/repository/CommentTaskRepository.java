package com.wfms.repository;

import com.wfms.entity.CommentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentTaskRepository extends JpaRepository<CommentTask,Long> {
    @Query(value = "SELECT * FROM comment_task where task_id = :taskId",nativeQuery = true)
    List<CommentTask> findByTaskId(@Param("taskId") Long taskId);


}
