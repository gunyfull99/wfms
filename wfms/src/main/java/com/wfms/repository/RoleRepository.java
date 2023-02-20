package com.wfms.repository;

import com.wfms.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Roles, Long> {
    @Query(value = "SELECT  * from roles where id NOT IN (select roles_id  from users_roles where users_id" +
            " = :id)" +
            "",nativeQuery = true)
    Set<Roles> getUserNotRole(@Param("id") long id);

    @Query(value = "SELECT  * from roles where id  IN (select roles_id  from users_roles where users_id" +
            " = :id)" +
            "",nativeQuery = true)
    List<Roles> getUserHaveRole(@Param("id") long id);

}
