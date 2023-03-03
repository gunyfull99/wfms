package com.wfms.service;

import com.wfms.entity.DailyReport;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DailyReportService {
    List<DailyReport> findAll();
    Page<DailyReport> findWithPage(int total, int page );

    List<DailyReport> findByPrọjectId(Long projectId);
}
