package com.wfms.repository;

import com.wfms.Dto.ChartResponseDto;

import java.util.List;

public interface TaskRepositoryCustom {
    List<ChartResponseDto> getstatisticTask(Long projectId);
}
