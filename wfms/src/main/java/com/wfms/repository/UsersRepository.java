package com.wfms.repository;

import com.wfms.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT  * from Users where username= :username", nativeQuery = true)
    Users findByUsername(@Param("username") String username);

    @Query(value = "SELECT  * from Users where id= :id", nativeQuery = true)
    Users selectById(@Param("id") Long id);

    @Query(value = "SELECT  FullName from Users where id= :id", nativeQuery = true)
    String findNameByUserId(@Param("id") long id);

    @Query(value = "select * from Users  where username   LIKE %:name% or FullName  LIKE %:name% ", nativeQuery = true)
    List<Users> searchUser(@Param("name") String name);

    @Query(value = "select id from Users  where  FullName  LIKE %:name% ", nativeQuery = true)
    List<Long> getListUserId(@Param("name") String name);
    Page<Users> findAllByRolesId(long id, Pageable p);
    Page<Users> findAllByFullNameContainingIgnoreCaseAndRolesIdAndStatus(String name, long roleId,int status, Pageable p);
    Page<Users> findAllByFullNameContainingIgnoreCaseAndRolesId(String name, long roleId, Pageable p);

    @Query(value = "SELECT * FROM Users \n" +
            " where lower(user_type) like :type and lower(full_name) like %:name%  and status=1  ", nativeQuery = true)
    Page<Users> filterWhereNoRole(@Param("name") String name,
             @Param("type") String type
            , Pageable pageable);

}
