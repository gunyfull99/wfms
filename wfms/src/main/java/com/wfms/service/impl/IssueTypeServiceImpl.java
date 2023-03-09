package com.wfms.service.impl;

import com.wfms.entity.IssueTypes;
import com.wfms.repository.IssueTypeRepository;
import com.wfms.service.IssueTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
@Service
public class IssueTypeServiceImpl implements IssueTypeService {

    @Autowired
    private IssueTypeRepository issueTypeRepository;

    @Override
    public IssueTypes createIssueType(IssueTypes issueTypes) {
        Assert.isTrue(Objects.nonNull(issueTypes.getIssueTypeName()),"Tên IssueType không được để trống");
        issueTypes.setIssueTypeId(null);
        issueTypes.setStatus(1);
        return issueTypeRepository.save(issueTypes);
    }

    @Override
    public List<IssueTypes> listIssueType() {
        return issueTypeRepository.findAll();
    }
}
