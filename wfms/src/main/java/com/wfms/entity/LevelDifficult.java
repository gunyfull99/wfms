package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "level_difficult")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDifficult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_difficult_id")
    private Long levelDifficultId;
    @Column(name = "level_difficult_name")
    private String levelDifficultName;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
}
