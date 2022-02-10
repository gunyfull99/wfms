package com.quiz.service;

import com.quiz.Dto.AccountDto;
import com.quiz.Dto.BaseResponse;
import com.quiz.Dto.CreateQuizForm;
import com.quiz.Dto.QuestDTO;
import com.quiz.entity.Question;
import com.quiz.entity.QuestionChoice;
import com.quiz.entity.Quiz;
import com.quiz.entity.QuizQuestion;
import com.quiz.exception.ResourceBadRequestException;
import com.quiz.repository.QuestionChoiceRepository;
import com.quiz.repository.QuizQuestionRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.restTemplate.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private QuestionChoiceRepository questionChoiceRepository;

    @Autowired
    RestTemplateService restTemplateService;

    public Quiz save(Quiz entity) {
        return quizRepository.save(entity);
    }

    public Quiz createQuiz(CreateQuizForm form) throws ResourceBadRequestException {
        Quiz quiz = form.getQuiz();
        Quiz quiz1 = new Quiz();
        LocalDateTime date1 = quiz.getStartTime();
        LocalDateTime date2 = quiz.getExpiredTime();
        if (date1.isAfter(date2)) {
            throw new ResourceBadRequestException(new BaseResponse(80805, "StartTime must before ExpireTime"));
        } else {
            quiz1.setNumberQuestions((int) form.getQuantity1() + (int) form.getQuantity2() + (int) form.getQuantity3());
            quiz1.setDescription(quiz.getDescription());
            quiz1.setStartTime(quiz.getStartTime());
            quiz1.setExpiredTime(quiz.getExpiredTime());
            quiz1.setStatus(quiz.getStatus());
            quiz1.setUserId(quiz.getUserId());
            return quizRepository.save(quiz1);
        }
    }

    public Quiz getDetailQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id).get();
        quiz.setQuestions(null);
        return quiz;
    }

    public Quiz addQuesToQuiz(CreateQuizForm form) {
        List<Question> hasTag1 = quesTionService.getAllQuestionByCate(form.getHasTag1());
        Collections.shuffle(hasTag1);
        List<Question> h1 = new ArrayList<>();
        Quiz quiz = createQuiz(form);
        int numberQuestion = quiz.getNumberQuestions();
        int totalTime = 0;
        for (int i = 0; i < form.getQuantity1(); i++) {
            if (hasTag1.get(i).getQuestionType().getId() == 3) {
                numberQuestion -= 1;
            }
            h1.add(hasTag1.get(i));
            totalTime += hasTag1.get(i).getQuestionTime();
        }
        List<Question> hasTag2 = quesTionService.getAllQuestionByCate(form.getHasTag2());
        Collections.shuffle(hasTag2);
        List<Question> h2 = new ArrayList<>();
        for (int i = 0; i < form.getQuantity2(); i++) {
            if (hasTag2.get(i).getQuestionType().getId() == 3) {
                numberQuestion -= 1;
            }
            h2.add(hasTag2.get(i));
            totalTime += hasTag2.get(i).getQuestionTime();
        }
        List<Question> hasTag3 = quesTionService.getAllQuestionByCate(form.getHasTag3());
        Collections.shuffle(hasTag3);
        List<Question> h3 = new ArrayList<>();
        for (int i = 0; i < form.getQuantity3(); i++) {
            if (hasTag3.get(i).getQuestionType().getId() == 3) {
                numberQuestion -= 1;
            }
            h3.add(hasTag3.get(i));
            totalTime += hasTag3.get(i).getQuestionTime();
        }
        List<Question> q = new ArrayList<>();
        q.addAll(h1);
        q.addAll(h2);
        q.addAll(h3);
        quiz.setNumberQuestions(numberQuestion);
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

    public Quiz calculateScore(List<QuestDTO> questDTO) {
        int score = 0;
        float percent = 0;
        String user_answer = "";
        List<QuizQuestion> questionIds = quizQuestionRepository.getListQuestionByQuizId(questDTO.get(0).getQuiz_id());
        for (int i = 0; i < questDTO.size(); i++) {
            if (questDTO.get(i).getQuestionType().getId() == 2) {
                int count = questionChoiceRepository.countCorrect(questDTO.get(i).getQuestions_id());
                int count1 = 0;
                for (int j = 0; j < questDTO.get(i).getQuestionChoiceDTOs().size(); j++) {
                    user_answer = user_answer + " ; " + questDTO.get(i).getQuestionChoiceDTOs().get(j).getId();

                    if (questDTO.get(i).getQuestionChoiceDTOs().get(j).isTrue() == true) {
                        count1 += 1;
                    }
                }
                String a = user_answer.replaceFirst(";", "").trim();

                if (count == count1) {
                    score += 1;
                    questionIds.get(i).setUser_answer(a);
                }
            } else if (questDTO.get(i).getQuestionType().getId() == 1) {
                questionIds.get(i).setUser_answer(questDTO.get(i).getQuestionChoiceDTOs().get(0).getId()+"");

                if (questDTO.get(i).getQuestionChoiceDTOs().get(0).isTrue() == true
                ) {
                    score += 1;
                }
            } else {
                questionIds.get(i).setUser_answer(questDTO.get(i).getQuestionChoiceDTOs().get(0).getText());
            }
            quizQuestionRepository.save(questionIds.get(i));
        }

        Quiz quiz = quizRepository.findById(questDTO.get(0).getQuiz_id()).get();
        percent = ((float) score / (float) quiz.getNumberQuestions()) * 100;
        quiz.setScore(score + "/" + quiz.getNumberQuestions() + "  (" + percent + "%)");
        quiz.setStatus("done");
        quiz.setEndTime(LocalDateTime.now());
        quizRepository.save(quiz);
        quiz.setQuestions(null);
        return quiz;
    }

    public List<Quiz> getListQuizByUserWhenDone(long id) {
        List<Quiz> list = quizRepository.getQuizByUserWhenDone(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

    public List<Quiz> getAllQuizByUser(long id) {
        List<Quiz> list = quizRepository.getAllByUser(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

    public List<Quiz> getListQuizNotStart(long id) {


        List<Quiz> list = quizRepository.getQuizNotStart(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

    public List<QuizQuestion> getListQuestionByQuizId(long quizId) {
        return quizQuestionRepository.getListQuestionByQuizId(quizId);
    }

    public List<QuestionChoice> getListChoiceByQuestionId(long id) {
        List<QuestionChoice> list = questionChoiceRepository.getListChoiceByQuesId(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestion(null);
        }
        return list;
    }

    public List<AccountDto> getUserDidTheTest() {
        List<Integer> userId = quizRepository.getIdByStatus();
        List<AccountDto> user = new ArrayList<>();
        for (Integer integer: userId) {
            AccountDto o =  restTemplateService.getName(integer);
            user.add(o);
        }
        return user;
    }
//    public List<Object> getName() {
//        List<Integer> accountId = quizRepository.getAllId();
//        List<Object> list = new ArrayList<>();
//        for (Integer integer : accountId) {
//            Object o = restTemplateService.getName(integer);
//            list.add(o);
//        }
//        return list;
//    }
}
