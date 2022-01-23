package com.quiz.repository;

import com.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {

    @Query(value = "INSERT INTO quiz_questions(questions_id, quiz_id,user_answer)VALUES (:quesId,:quizId,:ans)", nativeQuery = true)
    void addQuesTionToQuiz(@Param("quesId") Long quesId, @Param("quizId") Long quizId,@Param("ans") String ans);
}
