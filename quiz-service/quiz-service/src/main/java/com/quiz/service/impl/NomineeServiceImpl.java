package com.quiz.service.impl;

import com.quiz.Dto.NomineeEditRequest;
import com.quiz.Dto.NomineeRequest;
import com.quiz.entity.Nominee;
import com.quiz.repository.NomineeRepository;
import com.quiz.service.NomineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NomineeServiceImpl implements NomineeService {

    @Autowired
    NomineeRepository nomineeRepository;

    @Override
    public void createNominee(NomineeRequest nomineeRequest) {
        Nominee nomineeEntity = new Nominee();
        nomineeEntity.setName(nomineeRequest.getName());
        nomineeEntity.setStatus(nomineeRequest.isStatus());
        nomineeRepository.save(nomineeEntity);
    }
    @Override
    public List<Nominee> getAll(){
        return nomineeRepository.findAll();
    }

    @Override
    public void editNominee(NomineeEditRequest nomineeRequest) {
        Nominee nomineeEntity = nomineeRepository.getById(nomineeRequest.getId());

        nomineeEntity.setName(nomineeRequest.getName());
        nomineeEntity.setStatus(nomineeRequest.isStatus());
        nomineeRepository.save(nomineeEntity);
    }

    @Override
    public void deleteNominee(Long id) {
        Nominee nomineeEntity = nomineeRepository.getById(id);
        nomineeEntity.setStatus(false);
    }
}
