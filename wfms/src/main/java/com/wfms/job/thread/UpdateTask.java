package com.wfms.job.thread;

import com.wfms.entity.Priority;
import com.wfms.entity.Task;
import com.wfms.repository.TaskRepository;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class UpdateTask extends Thread{
    private TaskRepository taskRepository;
    private List<Task> listExtremeTask;
    private List<Task> listHighTaskt;
    private List<Task> listModerateTask;

    @Override
    public void run(){
        this.updateTaskPriority();
    }
    public void updateTaskPriority(){
        System.out.println("aaaaaaaa "+taskRepository);
        if(DataUtils.listNotNullOrEmpty(listExtremeTask)){
            for (Task task : this.listExtremeTask) {
                task.setPriority(Priority.builder().priorityId(Constants.EXTREME).build());
                taskRepository.save(task);
            }
        }
        if(DataUtils.listNotNullOrEmpty(listHighTaskt)){
            for (Task task : this.listHighTaskt) {
                task.setPriority(Priority.builder().priorityId(Constants.HIGH).build());
                taskRepository.save(task);
            }
        }
        if(DataUtils.listNotNullOrEmpty(listModerateTask)){
            for (Task task : this.listModerateTask) {
                task.setPriority(Priority.builder().priorityId(Constants.MODERATE).build());
                taskRepository.save(task);
            }
        }
    }
}
