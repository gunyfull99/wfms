package com.quiz.service;

import com.quiz.Dto.CreateQuizForm;
import com.quiz.Dto.QuestionEditRequest;
import com.quiz.Dto.QuestionRequest;
import com.quiz.Dto.QuestionTypeRequest;
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
    public List<QuestionRequest> getQuestionByCategory(String name) {
        List<Question> questionEntity = questionRepository.findByCategory_Name(name);
        List<QuestionRequest> questionRequests = new ArrayList<>();
        for(Question question : questionEntity){
            QuestionRequest request = new QuestionRequest();
            request.setContent(question.getContent());
            request.setQuestionType(question.getQuestionType());
            request.setCategory(question.getCategory());
//            request.setQuestionChoice(question.getQuestionChoice());
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
