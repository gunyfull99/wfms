package com.wfms.controller;

import com.wfms.Dto.ChartTask;
import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.TaskDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Task;
import com.wfms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @PostMapping("/create-task")
    public ResponseEntity<Object> createTask(@RequestHeader("Authorization") String token,@RequestBody TaskDTO task){
        try {
            return  ResponseEntity.ok().body(taskService.createTask(token, task));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-task-by-project")
    public ResponseEntity<Object> getTaskByProjectId(@RequestParam(name = "projectId") Long projectId){
        try {
            List<TaskDTO> taskList = taskService.getTaskByProjectId(projectId);
            return  ResponseEntity.ok().body(taskList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/search-task")
    public ResponseEntity<Object> searchTaskPaging(@RequestBody ObjectPaging objectPaging){
        try {
            ObjectPaging taskList = taskService.searchTask(objectPaging);
            return  ResponseEntity.ok().body(taskList);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/request-join-task")
    public ResponseEntity<Object> requestJoinTask(@RequestHeader("Authorization") String token,@RequestParam(name = "taskId") Long taskId){
        try {
            return  ResponseEntity.ok().body( taskService.requestToTask(token,taskId));
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-task-in-chart-in-sprint")
    public ResponseEntity<Object> listTaskInChartInSprint(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartTask>>  task = taskService.chartTask(projectId, false,3);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-task-in-chart-in-sprint-complete")
    public ResponseEntity<Object> listTaskInChartInSprintComplete(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartTask>>  task = taskService.chartTask(projectId, false,2);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-task-in-chart-in-backlog")
    public ResponseEntity<Object> listTaskInChartInBackLog(@RequestParam(name = "projectId") Long projectId){
        try {
            List<List<ChartTask>>  task = taskService.chartTask(projectId, true,0);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-task-by-id")
    public ResponseEntity<Object> getDetailTaskById(@RequestParam(name = "taskId") Long taskId){
        try {
            TaskDTO task = taskService.getDetailTaskById(taskId);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-task-in-backlog")
    public ResponseEntity<Object> getListTaskInBackLog(@RequestParam(name = "projectId") Long projectId){
        try {
            List<TaskDTO> task = taskService.getListTask(projectId,null);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-list-task-in-sprint")
    public ResponseEntity<Object> getListTaskInSprint(@RequestParam(name = "sprintId") Long sprintId){
        try {
            List<TaskDTO> task = taskService.getListTask(1L,sprintId);
            return  ResponseEntity.ok().body(task);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-task")
    public ResponseEntity<Object> updateTask(@RequestHeader("Authorization") String token,@RequestBody TaskDTO task){
        try {
            Task taskUpdate = taskService.updateTask(token, task);
            return  ResponseEntity.ok().body(taskUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update-assigness")
    public ResponseEntity<Object> updateAssigness(@RequestBody List<TaskUsers> taskUsers){
        try {
            List<TaskUsers> taskUpdate = taskService.updateAssignessTask(taskUsers);
            return  ResponseEntity.ok().body(taskUpdate);
        }catch (Exception e){
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-statistic-task")
    public ResponseEntity<Object> getstatisticTask(@RequestParam("projectId") Long projectId){
        try {
            List<ChartResponseDto> chartResponseDtos =  taskService.getstatisticTask(projectId);
            return new ResponseEntity<>(chartResponseDtos, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
