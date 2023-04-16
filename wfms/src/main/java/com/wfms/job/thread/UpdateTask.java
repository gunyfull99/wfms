package com.wfms.job.thread;

import com.wfms.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTask extends Thread{
    private List<Task> listTaskOneDay;
    private List<Task> listTaskThreeDay;
    private List<Task> listTaskFiveDay;
    private List<Task> listTaskSevenDay;

    @Override
    public void run(){
        this.updateTaskPriority();
    }
    public void updateTaskPriority(){

    }
}
