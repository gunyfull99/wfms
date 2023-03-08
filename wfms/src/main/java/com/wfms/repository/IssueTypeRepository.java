package com.wfms.repository;

import com.wfms.entity.IssueTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueTypeRepository extends JpaRepository<IssueTypes,Long> {
}
