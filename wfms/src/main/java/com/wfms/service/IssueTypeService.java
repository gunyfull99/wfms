package com.wfms.service;


import com.wfms.entity.IssueTypes;

import java.util.List;

public interface IssueTypeService {
    IssueTypes createIssueType (IssueTypes issueTypes);
    List<IssueTypes> listIssueType ();
}
