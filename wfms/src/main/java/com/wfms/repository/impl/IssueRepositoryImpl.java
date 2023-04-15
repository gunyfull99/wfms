package com.wfms.repository.impl;

import com.wfms.Dto.ChartResponseDto;
import com.wfms.repository.IssueRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
@Repository
public class IssueRepositoryImpl implements IssueRepositoryCustom {
    @PersistenceContext
    private EntityManager em;


    @Override
    public List<ChartResponseDto> getstatisticTask(Long projectId) {
        String queryStr = "select \n" +
                "        EXTRACT('MONTH' FROM iss.created_date) as months,\n" +
                "        count(issue_id) as totalTask,\n" +
                "        sum(case when iss.is_archived = 'true' then 1 else 0 end) as taskDone,\n" +
                "        sum(case when iss.is_archived = 'false' then 1 else 0 end) as taskNotDone\n" +
                "        from issue iss where iss.project_id = :projectId group by EXTRACT('MONTH' FROM iss.created_date) \n" +
                "        order by EXTRACT('MONTH' FROM iss.created_date) desc";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        return parseResult(query.getResultList());
    }
    private List<ChartResponseDto> parseResult(List<Object[]> lst){
        List<ChartResponseDto> chartResponseDtos = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseDtos.add(ChartResponseDto.builder()
                            .month(Double.valueOf(item[0].toString()))
                            .totalTask(Double.valueOf(item[1].toString()))
                            .totalTaskDone(Double.valueOf( item[2].toString()))
                            .totalTaskNotDone(Double.valueOf( item[3].toString()))
                            .build());
        }
        return chartResponseDtos;
    }
}
