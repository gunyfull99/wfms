package com.wfms.repository;

import com.wfms.entity.DeviceUsers;
import com.wfms.entity.RequestTask;
import com.wfms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestTaskRepository extends JpaRepository<RequestTask,Long> {
    @Query(value = "SELECT * FROM request_task where request_task_id = :id and status in (:status) ",nativeQuery = true)
    List<RequestTask> getRequestTaskInStatus(@Param("id") Long id,@Param("status") List<Integer> status);
    @Query(value = "Select i from RequestTask i where  " +
            " (:status is null OR (i.status) = :status) ")
    Page<RequestTask> searchRequestTask(@Param("status") Integer status, Pageable pageable);

}
