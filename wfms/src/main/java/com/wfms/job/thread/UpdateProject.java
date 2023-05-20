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
    private ProjectRepository projectRepository;
    private List<Projects> listExtremeProject;
    private List<Projects> listHighProject;
    private List<Projects> listModerateProject;
    @Override
    public void run(){
        this.updateProjectPriority();
    }

    public void updateProjectPriority(){
        if(DataUtils.listNotNullOrEmpty(listExtremeProject)){
            for (Projects project : this.listExtremeProject) {
                  project.setPriorityId(Constants.EXTREME);
                projectRepository.save(project);
            }
        }
        if(DataUtils.listNotNullOrEmpty(listHighProject)){
            for (Projects project : this.listHighProject) {
                project.setPriorityId(Constants.HIGH);
                projectRepository.save(project);
            }
        }
        if(DataUtils.listNotNullOrEmpty(listModerateProject)){
            for (Projects project : this.listModerateProject) {
                project.setPriorityId(Constants.MODERATE);
                projectRepository.save(project);
            }
        }
    }
}
