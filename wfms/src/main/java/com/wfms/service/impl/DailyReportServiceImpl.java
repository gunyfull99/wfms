package com.wfms.service.impl;

import com.wfms.entity.DailyReport;
import com.wfms.repository.DailyReportRepository;
import com.wfms.service.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DailyReportServiceImpl implements DailyReportService {
    @Autowired
    private DailyReportRepository dailyReportRepository;
    public List<DailyReport> findAll(){
        return dailyReportRepository.findAll();
    }


    @Override
    public Page<DailyReport> findWithPage(int total, int page) {
        Pageable pageableRequest = PageRequest.of(page, total, Sort.Direction.DESC);
        return dailyReportRepository.findAll(pageableRequest);
    }

    @Override
    public List<DailyReport> findByPrọjectId(Long projectId) {
        return dailyReportRepository.findByProjectId(projectId);
    }

}
