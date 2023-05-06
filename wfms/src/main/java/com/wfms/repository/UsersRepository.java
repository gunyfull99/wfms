package com.wfms.repository;

import com.wfms.entity.Projects;
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

    @Query(value = "SELECT  * from users where username= :username", nativeQuery = true)
    Users findByUsername(@Param("username") String username);

    @Query(value = "SELECT  * from users where email_address = :mail", nativeQuery = true)
    Users findByMail(@Param("mail") String mail);
    @Query(value = "SELECT  * from users order by id desc limit 1", nativeQuery = true)
    Users getLasUser();

    @Query(value = "SELECT  * from users where id= :id", nativeQuery = true)
    Users selectById(@Param("id") Long id);

    @Query(value = "SELECT  full_name from users where id= :id", nativeQuery = true)
    String findNameByUserId(@Param("id") long id);

    @Query(value = "SELECT * from users where id not in ( select distinct user_id from project_users where status = 1) and job_title not in('ADMIN','PM') order by id desc", nativeQuery = true)
    List<Users> findUserNotInProject();

    @Query(value = "select * from users  where email_address LIKE %:name% or full_name  LIKE %:name% ", nativeQuery = true)
    List<Users> searchUser(@Param("name") String name);

    @Query(value = "select id from users  where  full_name  LIKE %:name% ", nativeQuery = true)
    List<Long> getListUserId(@Param("name") String name);
    Page<Users> findAllByRolesId(long id, Pageable p);
    Page<Users> findAllByFullNameContainingIgnoreCaseAndRolesIdAndStatus(String name, long roleId,int status, Pageable p);
    Page<Users> findAllByFullNameContainingIgnoreCaseAndRolesId(String name, long roleId, Pageable p);

    Page<Users> findAllByFullNameOrEmailAddressContainingIgnoreCase(String name,String email, Pageable p);

    @Query(value = "SELECT * FROM users \n" +
            " where lower(user_type) like :type and lower(full_name) like %:name%  and status=1  ", nativeQuery = true)
    Page<Users> filterWhereNoRole(@Param("name") String name,
             @Param("type") String type
            , Pageable pageable);
    Users findByEmailAddress(String email);

    @Query(value = "Select p from Users p where  " +
            "(:status is null OR (p.status) = :status) " +
            "and ( (:list) is null OR p.id in :list) " +
            "and (:keyword is null OR LOWER(p.emailAddress) LIKE %:keyword% " +
            "or LOWER(p.address) LIKE %:keyword% " +
            "or LOWER(p.jobTitle) LIKE %:keyword% " +
            "or LOWER(p.fullName) LIKE %:keyword% ) ")
    Page<Users> searchUsers(@Param("list") List<Long> users, @Param("status") Integer status, @Param("keyword") String keyword, Pageable pageable);
}
