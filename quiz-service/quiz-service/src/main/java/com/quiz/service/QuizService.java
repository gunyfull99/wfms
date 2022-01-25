package com.quiz.service;

import com.quiz.Dto.CreateQuizForm;
import com.quiz.Dto.QuizQuestionForm;
import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import com.quiz.entity.QuizQuestion;
import com.quiz.repository.QuizQuestionRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.restTemplate.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @Autowired
    private QuesTionService quesTionService;
    @Autowired
    RestTemplateService restTemplateService;

    public Quiz save(Quiz entity) {
        return quizRepository.save(entity);
    }

    public Quiz createQuiz(CreateQuizForm form) {
        Quiz quiz = form.getQuiz();
        Quiz quiz1 = new Quiz();
        quiz1.setNumberQuestions((int) form.getQuantity1() + (int) form.getQuantity2() + (int) form.getQuantity3());
        quiz1.setDescription(quiz.getDescription());
        quiz1.setStatus(quiz.getStatus());
        quiz1.setUserId(quiz.getUserId());
        return quizRepository.save(quiz1);
    }

    public Quiz getDetailQuiz(Long id) {
        return quizRepository.getById(id);
    }

    public Quiz addQuesToQuiz(CreateQuizForm form) {
        List<Question> hasTag1 = quesTionService.getAllQuestionByCate(form.getHasTag1());
        Collections.shuffle(hasTag1);
        List<Question> h1 = new ArrayList<>();
        int totalTime = 0;
        for (int i = 0; i < form.getQuantity1(); i++) {
            h1.add(hasTag1.get(i));
            totalTime += hasTag1.get(i).getQuestionTime();
        }
        List<Question> hasTag2 = quesTionService.getAllQuestionByCate(form.getHasTag2());
        Collections.shuffle(hasTag2);
        List<Question> h2 = new ArrayList<>();
        for (int i = 0; i < form.getQuantity2(); i++) {
            h2.add(hasTag2.get(i));
            totalTime += hasTag2.get(i).getQuestionTime();
        }
        List<Question> hasTag3 = quesTionService.getAllQuestionByCate(form.getHasTag3());
        Collections.shuffle(hasTag3);
        List<Question> h3 = new ArrayList<>();
        for (int i = 0; i < form.getQuantity3(); i++) {
            h3.add(hasTag3.get(i));
            totalTime += hasTag3.get(i).getQuestionTime();
        }
        List<Question> q = new ArrayList<>();
        q.addAll(h1);
        q.addAll(h2);
        q.addAll(h3);
        Quiz quiz = createQuiz(form);
        quiz.setQuizTime(totalTime);
        quizRepository.save(quiz);

        for (int i = 0; i < q.size(); i++) {
            QuizQuestion q1 = new QuizQuestion();
            q1.setQuestions_id(q.get(i).getId());
            q1.setQuiz_id(quiz.getId());
            q1.setUser_answer("not yet");
            quizQuestionRepository.save(q1);
        }
        return quiz;
    }

    public int calculateScore(Quiz quiz) {
        int score = 0;
        float percent=0;

        List<Question> questionBody = quiz.getQuestions();
        List<Question> questionsDB = quizRepository.getById(quiz.getId()).getQuestions();
        for (int i = 0; i < questionBody.size(); i++) {
            if (questionBody.get(i).getQuestionChoice().get(i).isTrue() ==
                    questionsDB.get(i).getQuestionChoice().get(i).isTrue()
            ) {
                score += 1;
            }
//            QuizQuestion qq=new QuizQuestion();
//            qq.setUser_answer("aa");
//            qq.setQuiz_id(quiz.getId());
//            qq.setQuestions_id(questionBody.get(i).getId());
//            quizQuestionRepository.save(qq);
        }
        percent=(score/quiz.getNumberQuestions())*100;
        quiz.setScore(score+"/"+quiz.getNumberQuestions()+"  ("+percent+"%)");
        quiz.setStatus("done");
         quizRepository.save(quiz);
        return   score;
    }

    public List<Quiz> getListQuizByUser(long id){
        return quizRepository.getQuizByUser(id);
    }
    public List<Quiz> getListQuizNotStart(long id){
        return quizRepository.getQuizNotStart(id);
    }

    public List<QuizQuestion> getDetailQuizByUser(long quizId){
        return quizQuestionRepository.getDetailQuizByUser(quizId);
    }
//    private long questions_id;
//    private long quiz_id;
//    private String user_answer;
//    private Question question;

    public List<QuizQuestionForm> getDetailByUser1(long quizId){
        List<QuizQuestion> q1= getDetailQuizByUser(quizId);
        List<QuizQuestionForm> q2=new ArrayList<>();
        for (int i = 0; i <q1.size() ; i++) {
            q2.add(new QuizQuestionForm( q1.get(i).getQuestions_id(),q1.get(i).getQuiz_id()
            ,q1.get(i).getUser_answer(),quesTionService.getQuestionById(q1.get(i).getQuestions_id())));
        }
        return q2;
    }
    public List<Object> getName() {
        List<Integer> accountId = quizRepository.getAllId();
        List<Object> list = new ArrayList<>();
        for (Integer integer: accountId){
            Object o = restTemplateService.getName(integer);
            list.add(o);
        }
        return list;
    }
}
