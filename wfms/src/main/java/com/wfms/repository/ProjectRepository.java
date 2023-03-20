package com.wfms.repository;

import com.wfms.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Projects,Long> {
    @Query(value = "SELECT * FROM projects WHERE dead_line = :deadLine",nativeQuery = true)
    List<Projects> getProjectByDeadline(@Param("deadLine") Date deadLine);
}
