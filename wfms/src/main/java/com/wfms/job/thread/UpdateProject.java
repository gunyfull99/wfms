package com.wfms.job.thread;

import com.wfms.entity.Projects;
import com.wfms.repository.ProjectRepository;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProject extends Thread{
    @Autowired
    private ProjectRepository projectRepository;
    private List<Projects> listProjectOneMonth;
    private List<Projects> listProjectOneWeek;
    private List<Projects> listProjectTwoWeek;
    @Override
    public void run(){
        this.updateProjectPriority();
    }

    public void updateProjectPriority(){
        if(DataUtils.notNull(listProjectOneMonth)){
            for (Projects project : this.listProjectOneMonth) {
             //   project.setPriorityId(Constants.DeadlineIn);
                projectRepository.save(project);
            }
        }
        if(DataUtils.notNull(listProjectOneWeek)){
            for (Projects project : this.listProjectOneMonth) {
               // project.setPriorityId(Constants.HOT);
                projectRepository.save(project);
            }
        }
        if(DataUtils.notNull(listProjectTwoWeek)){
            for (Projects project : this.listProjectOneMonth) {
             //   project.setPriorityId(Constants.WARNING);
                projectRepository.save(project);
            }
        }
    }
}
