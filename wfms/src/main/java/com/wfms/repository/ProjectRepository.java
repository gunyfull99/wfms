package com.wfms.repository;

import com.wfms.entity.Projects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "Select p from Projects p where  " +
            " (:status is null OR (p.status) = :status) " +
            "and (:keyword is null OR LOWER(p.projectName) LIKE %:keyword% " +
            "or LOWER(p.shortName) LIKE %:keyword% ) ")
    Page<Projects> getProjectsByAdmin(@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "Select p from Projects p where  " +
            " ((p.lead)= :lead) " +
            "and (:status is null OR (p.status) = :status) " +
            "and (:keyword is null OR LOWER(p.projectName) LIKE %:keyword% " +
            "or LOWER(p.shortName) LIKE %:keyword% ) ")
    Page<Projects> getProjectsByLead(@Param("lead") Long lead,@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "Select p from Projects p where  " +
            " projectId in :projects " +
            "and (:status is null OR (p.status) = :status) " +
            "and (:keyword is null OR LOWER(p.projectName) LIKE %:keyword% " +
            "or LOWER(p.shortName) LIKE %:keyword% ) ")
    Page<Projects> getProjectsByMember(@Param("projects") List<Long> projects,@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);
}
