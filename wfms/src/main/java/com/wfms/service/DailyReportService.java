package com.wfms.service;

import com.wfms.Dto.DailyReportDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.DailyReport;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DailyReportService {
    List<DailyReport> findAll();
    Page<DailyReport> findWithPage(int total, int page );
    List<DailyReport> findByProjectId(Long projectId);
    DailyReportDTO createDailyReport(String token,DailyReportDTO dailyReport);
    DailyReportDTO getDetailDailyReport(Long dailyReportId);
    DailyReport updateDailyReport(DailyReport dailyReport);
    ObjectPaging searchDaiLyReport(ObjectPaging objectPaging);

}
