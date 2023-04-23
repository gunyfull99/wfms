package com.wfms.repository;

import com.wfms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    @Query(value = "SELECT task.* FROM task, task_users, users where users.id = " +
            "task_users.user_id and task.task_id =task_users.task_id and users.id = :userId",nativeQuery = true)
    List<Task> getTaskByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT task.* FROM task, task_users, users where users.id = " +
            "task_users.user_id and task.task_id =task_users.task_id and users.id = :userId and task.project_id = :projectId",nativeQuery = true)
    List<Task> getTaskByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query(value = "Select * from task where project_id = :projectId",nativeQuery = true)
    List<Task> getTaskByProjectId(@Param("projectId") Long projectId);

    @Query(value = "Select * from task where project_id = :projectId and status IN(1,3)",nativeQuery = true)
    List<Task> getTaskByProjectIdAndStatus(@Param("projectId") Long projectId);
    @Query(value = "Select i from Task i where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:createByPm is null OR (i.createByPm) = :createByPm) " +
            "and (:stepId is null OR (i.workFlowStepId) = :stepId) " +
            "and (:sprintId is null OR (i.sprint.sprintId) = :sprintId) " +
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.summary) LIKE %:keyword% " +
            "or  LOWER(i.code) LIKE %:keyword% ) ")
    Page<Task> searchTaskPaging(@Param("projectId") Long projectId, @Param("status") Integer status, @Param("keyword") String keyword, @Param("sprintId") Long sprintId, @Param("stepId") Long stepId, @Param("createByPm") Boolean createByPm, Pageable pageable);
    @Query(value = "Select i from Task i where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:createByPm is null OR (i.createByPm) = :createByPm) " +
            "and (:stepId is null OR (i.workFlowStepId) = :stepId) " +
            "and (:sprintId is null OR (i.sprint.sprintId) is null ) " +
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.summary) LIKE %:keyword% " +
            "or  LOWER(i.code) LIKE %:keyword% ) ")
    Page<Task> searchTaskPagingBackLog(@Param("projectId") Long projectId, @Param("status") Integer status, @Param("keyword") String keyword, @Param("sprintId") Long sprintId, @Param("stepId") Long stepId, @Param("createByPm") Boolean createByPm, Pageable pageable);
    @Query(value = "Select i from Task i where  (:sprintId is null OR i.sprint.sprintId = :sprintId) ")
    List<Task> getListTaskInSprint(@Param("sprintId") Long sprintId);

    @Query(value = "Select i from Task i where  (:sprintId is null OR i.sprint.sprintId = :sprintId) and i.workFlowStepId = :step")
    List<Task> getListTaskInSprintAndStep(@Param("sprintId") Long sprintId, @Param("step") Long step);

    @Query(value = "Select i from Task i where  ((i.sprint.sprintId) is null ) and i.workFlowStepId = :step")
    List<Task> getListTaskInBackLogAndStep(@Param("step") Long step);

    @Query(value = "Select i from Task i where  (:sprintId is null OR i.sprint.sprintId = :sprintId) and i.status != 2 ")
    List<Task> getListTaskInSprintAndClose(@Param("sprintId") Long sprintId);

    @Query(value = "Select * from task where  sprint_id is null and project_id = :projectId",nativeQuery = true)
    List<Task> getListTaskInBackLog(@Param("projectId") Long projectId);

    @Query(value = "Select count(*) from task where project_id = :projectId",nativeQuery = true)
    Integer getCountTaskByProject(@Param("projectId") Long projectId);

    @Query(value = "Select * from task where dead_line = :deadLine",nativeQuery = true)
    List<Task> getTaskByDeadline(@Param("deadLine") Date deadLine);

    @Query(value = "Select * from task where status = 3",nativeQuery = true)
    List<Task> getListTaskActive();

}

