package com.wfms.repository;

import com.wfms.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    public List<Sprint> findSprintByProjectId(Long project_id);
}
