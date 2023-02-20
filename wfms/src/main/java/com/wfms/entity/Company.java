package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "company")
public class Company {


    @Id
    @SequenceGenerator(name="company_generator",sequenceName="company_seq")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="company_generator")
    private long id;
    private String name;
    private String email;
    private String phone;
    private String shortCutName;
    private String address;
    private String logo;

   @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
   @JsonIgnore
    private Set<Users> users =new HashSet<>();
}
