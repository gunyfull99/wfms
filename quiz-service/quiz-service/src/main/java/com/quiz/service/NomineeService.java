package com.quiz.service;

import com.quiz.Dto.NomineeEditRequest;
import com.quiz.Dto.NomineeRequest;

public interface NomineeService {
    void createNominee(NomineeRequest nomineeRequest);

    void editNominee(NomineeEditRequest nomineeRequest);

    void deleteNominee(Long id);
}
