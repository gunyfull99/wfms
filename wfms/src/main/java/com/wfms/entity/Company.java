//package com.wfms.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "company")
//public class Company {
//
//
//    @Id
//    @SequenceGenerator(name="company_generator",sequenceName="company_seq")
//    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="company_generator")
//    private Long id;
//    private String name;
//    private String email;
//    private String phone;
//    private String shortCutName;
//    private String address;
//    private String logo;
//    private Integer status;
//   @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//   //@JsonBackReference
//   @JsonIgnore
//    private Set<Users> users =new HashSet<>();
//    @Column(name = "create_date")
//    private Date createDate;
//    @Column(name = "update_date")
//    private Date updateDate;
//}
