package com.quiz.service;

import com.quiz.Dto.*;
import com.quiz.entity.Question;
import com.quiz.entity.QuestionChoice;
import com.quiz.entity.QuestionType;
import com.quiz.repository.CategoryRepository;
import com.quiz.repository.QuestionChoiceRepository;
import com.quiz.repository.QuestionRepository;
import com.quiz.repository.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuesTionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    QuestionChoiceRepository questionChoiceRepository;
    @Autowired
    QuestionTypeRepository questionTypeRepository;
    public List<Question> getAllQuestionByCate(long id){
                return questionRepository.getAllQuestionByCate(id);
    }

    public List<Question> findAllById(long id){
        return questionRepository.findAllById(Collections.singleton(id));
    }
    public Question getQuestionById(long id){
        return questionRepository.getById(id);
    }
    public void createQuestion(QuestionRequest request) {

        if(questionRepository.findByContent(request.getContent())!=null){
            throw new RuntimeException("this question was crate before !!!");
        }

        Question questionEntity = new Question();
        questionEntity.setContent(request.getContent());
        questionEntity.setQuestionType(request.getQuestionType());
        questionEntity.setCategory(request.getCategory());
        questionEntity.setQuestionChoice(request.getQuestionChoice());
        questionEntity.setQuestionTime(request.getQuestionTime());
        Question q1   =questionRepository.save(questionEntity);
        List<QuestionChoice> q=request.getQuestionChoice();
        for (int i = 0; i < q.size(); i++) {
         q.get(i).setQuestion(q1);
            questionChoiceRepository.save(q.get(i));
        }


    }
    public List<QuestionRequest> getAllQuestion() {
        List<Question> question = questionRepository.findAll();
        List<QuestionRequest> questionRequests = new ArrayList<>();
        for (Question question1: question) {
            QuestionRequest request = new QuestionRequest();
            request.setContent(question1.getContent());
            request.setQuestionType(question1.getQuestionType());
            request.setCategory(question1.getCategory());
            request.setQuestionChoice(question1.getQuestionChoice());
            request.setQuestionTime(question1.getQuestionTime());
            questionRequests.add(request);
        }
        return questionRequests;
    }
    public void editQuestion(QuestionEditRequest request) {
        Question questionEntity = questionRepository.getById(request.getId());
        questionEntity.setContent(request.getContent());
        questionEntity.setQuestionType(request.getQuestionType());
        questionEntity.setCategory(request.getCategory());
        questionEntity.setQuestionChoice(request.getQuestionChoice());
        questionEntity.setQuestionTime(request.getQuestionTime());
        questionRepository.save(questionEntity);
    }
    public List<QuestDTO> getQuestionByCategory(long id) {
        List<Question> questionEntity = questionRepository.getAllQuestionByCate(id);
        List<QuestDTO> questionRequests = new ArrayList<>();

        for(Question question : questionEntity){
            QuestDTO request = new QuestDTO();
            request.setContent(question.getContent());
            request.setQuestionType(question.getQuestionType());
            request.setCategory(question.getCategory());
            List<QuestionChoiceDTO> questionChoiceDTOS = new ArrayList<>();
            for(QuestionChoice questionChoice : question.getQuestionChoice()){
                QuestionChoiceDTO questionChoiceDTO = new QuestionChoiceDTO();
                questionChoiceDTO.setId(questionChoice.getId());
                questionChoiceDTO.setName(questionChoice.getName());
                questionChoiceDTO.setResult(questionChoice.isTrue());
                questionChoiceDTOS.add(questionChoiceDTO);
            }
            request.setQuestionChoiceDTOs(questionChoiceDTOS);
            request.setQuestionTime(question.getQuestionTime());
            questionRequests.add(request);
        }
        return questionRequests;
    }
    public void createQuestionType(QuestionTypeRequest type) {
        QuestionType questionType = new QuestionType();
        questionType.setName(type.getName());
        questionTypeRepository.save(questionType);
    }

    public List<QuestionType> getAllQuestionType() {
        List<QuestionType> question = questionTypeRepository.findAll();
        return question;
    }

}
