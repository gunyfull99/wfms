//package com.wfms.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import java.util.Date;
//
//@Entity
//@Data
//@Table(name = "users_roles")
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UsersRoles {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "users_roles_id")
//    private Long usersRolesId;
//    @Column(name = "roles_id")
//    private Long rolesId;
//    @Column(nullable = false,name ="users_id" )
//    private Long userId;
//    @Column(name ="start_date" )
//    private Date startDate;
//    @Column(name ="end_date" )
//    private Date endDate;
//
//}
