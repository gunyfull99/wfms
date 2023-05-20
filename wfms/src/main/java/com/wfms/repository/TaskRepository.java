package com.wfms.repository;

import com.wfms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

 import java.time.LocalDateTime; 
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    @Query(value = "SELECT task.* FROM task, task_users, users where users.id = " +
            "task_users.user_id and task.task_id =task_users.task_id and users.id = :userId and task.status =3 ",nativeQuery = true)
    List<Task> getTaskByUserId(@Param("userId") Long userId);
    @Query(value = "SELECT task.* FROM task, task_users, users where task.status = 3 and users.id = " +
            "task_users.user_id and task.task_id =task_users.task_id and users.id = :userId and task.project_id = :projectId",nativeQuery = true)
    List<Task> getTaskByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query(value = "Select * from task where project_id = :projectId",nativeQuery = true)
    List<Task> getTaskByProjectId(@Param("projectId") Long projectId);

    @Query(value = "Select * from task where project_id = :projectId and status IN(1,3)",nativeQuery = true)
    List<Task> getTaskByProjectIdAndStatus(@Param("projectId") Long projectId);
    @Query(value = "Select i from Task i  where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:createByPm is null OR (i.createByPm) = :createByPm) " +
            "and (:stepId is null OR (i.workFlowStepId) = :stepId) " +
            "and (:taskType is null OR (i.taskTypeId) = :taskType) " +
            "and (:priority is null OR (i.priority.priorityId) = :priority) " +
            "and (:userId is null OR (i.assigness) = :userId) " +
            "and (:level is null OR (i.levelDifficultId) = :level) " +
            "and (:sprintId is null OR (i.sprint.sprintId) = :sprintId) " +
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.summary) LIKE %:keyword% " +
            "or  LOWER(i.code) LIKE %:keyword% ) ")
    Page<Task> searchTaskPaging(@Param("projectId") Long projectId,
                                @Param("status") Integer status,
                                @Param("keyword") String keyword,
                                @Param("sprintId") Long sprintId,
                                @Param("stepId") Long stepId,
                                @Param("createByPm") Boolean createByPm,
                                @Param("taskType") Long taskType,
                                @Param("priority") Long priority,
                                @Param("userId") Long userId,
                                @Param("level") Long level
            , Pageable pageable);
    @Query(value = "Select distinct(i) from Task i join TaskUsers tu  on i.taskId = tu.taskId where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and (:status is null OR (i.status) = :status) " +
            "and (:createByPm is null OR (i.createByPm) = :createByPm) " +
            "and (:stepId is null OR (i.workFlowStepId) = :stepId) " +
            "and (:taskType is null OR (i.taskTypeId) = :taskType) " +
            "and (:priority is null OR (i.priority.priorityId) = :priority) " +
            "and (:userId is null OR (i.assigness) = :userId or (tu.userId = :userId  and (tu.status) = 2) ) " +
            "and (:level is null OR (i.levelDifficultId) = :level) " +
            "and (:sprintId is null OR (i.sprint.sprintId) = :sprintId) " +
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.summary) LIKE %:keyword% " +
            "or  LOWER(i.code) LIKE %:keyword% ) ")
    Page<Task> searchTaskPagingWithReport(@Param("projectId") Long projectId,
                                @Param("status") Integer status,
                                @Param("keyword") String keyword,
                                @Param("sprintId") Long sprintId,
                                @Param("stepId") Long stepId,
                                @Param("createByPm") Boolean createByPm,
                                @Param("taskType") Long taskType,
                                @Param("priority") Long priority,
                                @Param("userId") Long userId,
                                @Param("level") Long level
            , Pageable pageable);
    @Query(value = "Select i from Task i   where  " +
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
    List<Task> getTaskByDeadline(@Param("deadLine") LocalDateTime deadLine);

    @Query(value = "Select * from task where status = 3",nativeQuery = true)
    List<Task> getListTaskActive();

    @Query(value = "Select * from task where  parent = :parent and status = 3 ",nativeQuery = true)
    List<Task> getListTaskByParent(@Param("parent") Long parent);

    @Query(value = "Select * from task where work_flow_step_id = :stepId and status != 0 ",nativeQuery = true)
    List<Task> getListTaskByStep(@Param("stepId") Long stepId);

    @Query(value = "Select t.* from task t join task_users \n" +
            " tu on t.task_id=tu.task_id where t.work_flow_step_id = :stepId \n" +
            " and t.status = 3 and tu.user_id=:userId and tu.status =2",nativeQuery = true)
    List<Task> getListTaskByUserAndStep(@Param("stepId") Long stepId,@Param("userId") Long userId);

    @Query(value = "    Select t.* from task t join task_users\n" +
            "        tu on t.task_id=tu.task_id  where t.level_difficult_id = :levelId\n" +
            "        and t.status = 3 and tu.user_id= :userId and tu.status =2 ",nativeQuery = true)
    List<Task> getListTaskByUserAndLevel(@Param("levelId") Long levelId,@Param("userId") Long userId);
}

