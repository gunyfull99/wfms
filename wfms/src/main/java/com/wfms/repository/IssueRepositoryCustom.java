package com.wfms.repository;

import com.wfms.Dto.ChartResponseDto;

import java.util.List;

public interface IssueRepositoryCustom {
    List<ChartResponseDto> getstatisticTask(Long projectId);
}
