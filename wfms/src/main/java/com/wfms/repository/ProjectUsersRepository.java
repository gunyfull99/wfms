package com.wfms.repository;

import com.wfms.entity.ProjectUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectUsersRepository extends JpaRepository<ProjectUsers,Long> {
}
