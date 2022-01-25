package com.quiz.service;

import com.quiz.Dto.NomineeEditRequest;
import com.quiz.Dto.NomineeRequest;
import com.quiz.entity.Nominee;

import java.util.List;

public interface NomineeService {
    void createNominee(NomineeRequest nomineeRequest);

    void editNominee(NomineeEditRequest nomineeRequest);
     List<Nominee> getAll();
    void deleteNominee(Long id);
}
