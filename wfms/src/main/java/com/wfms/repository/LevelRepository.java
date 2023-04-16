package com.wfms.repository;

import com.wfms.entity.LevelDifficult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LevelRepository extends JpaRepository<LevelDifficult,Long> {
}
