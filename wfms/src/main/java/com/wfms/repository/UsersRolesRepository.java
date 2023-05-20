//package com.wfms.repository;
//
//import com.wfms.entity.Sprint;
//import com.wfms.entity.UsersRoles;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
// import java.time.LocalDateTime; 
//import java.util.List;
//
//public interface UsersRolesRepository extends JpaRepository<UsersRoles, Long> {
//
//    @Query(value = "Select * from users_roles where users_id = :userId ",nativeQuery = true)
//    List<UsersRoles> getListRoleByUser(@Param("userId") Long userId);
//
//    @Query(value = "Select * from users_roles where users_id = :userId and roles_id = :roleId",nativeQuery = true)
//    UsersRoles getByUserAndRole(@Param("userId") Long userId,@Param("roleId") Long roleId);
//    @Query(value = "Select * from users_roles order by users_roles_id desc limit 1",nativeQuery = true)
//    UsersRoles getLastUserRole();
//    @Query(value = "insert into users_roles(users_roles_id,users_id,roles_id,start_date) VALUES(:userRoleId,:userId,:roleId,:date)", nativeQuery = true)
//    void addUserRole(@Param("userRoleId") Long userRoleId,@Param("userId") Long userId, @Param("roleId")Long roleId, @Param("date")Date date);
//
//}
