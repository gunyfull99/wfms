package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseValidateUserDTO {
    private List<UserValidateDto> listUserValidate;
    private Integer total;
    private Integer totalPass;
    private Integer totalFail;
}
