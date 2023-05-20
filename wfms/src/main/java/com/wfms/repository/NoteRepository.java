package com.wfms.repository;

import com.wfms.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    @Query(value = "Select i from Note i where  " +
            " (:projectId is null OR (i.projectId)= :projectId)" +
            "and ((i.status) = 1) " +
            "and (:keyword is null OR LOWER(i.title) LIKE %:keyword% " +
            "or  LOWER(i.content) LIKE %:keyword% ) ")
    Page<Note> searchNotePaging(@Param("projectId") Long projectId,
                                @Param("keyword") String keyword
            , Pageable pageable);
}
