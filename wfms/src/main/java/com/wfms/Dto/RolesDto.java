package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

 import java.time.LocalDateTime; 

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesDto {
    private Long id;
    private String name;
    private Integer status;
}
