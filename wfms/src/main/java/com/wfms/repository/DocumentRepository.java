package com.wfms.repository;

import com.wfms.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query(value = "Select i from Document i where  " +
            " (:projectId is null OR (i.projects.projectId)= :projectId)"+
            "and (:keyword is null OR LOWER(i.description) LIKE %:keyword% " +
            "or LOWER(i.fileName) LIKE %:keyword% " +
            "or  LOWER(i.type) LIKE %:keyword% ) ")
    Page<Document> getListFileInProject(@Param("projectId") Long projectId,@Param("keyword") String keyword, Pageable pageable);
}
