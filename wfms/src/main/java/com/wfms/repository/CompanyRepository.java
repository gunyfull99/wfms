package com.wfms.repository;

import com.wfms.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query(value = "select * from company where id = :id",nativeQuery = true)
    Company findComPanyById(@Param("id") Long id);
}
