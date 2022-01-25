package com.quiz.repository;

import com.quiz.entity.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice,Long> {

}
