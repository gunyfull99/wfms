package com.quiz.repository;

import com.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query(value = "select * from questions where cate_id = :id", nativeQuery = true)
    List<Question> getAllQuestionByCate(@Param("id") long id );

    Question findByContent(String content);
    List<Question> findByCategory_Name(String name);
}
