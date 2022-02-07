package com.quiz.Dto;

import com.quiz.entity.QuestionChoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateForm {
    private List<QuestionChoice> questionChoices;


}
