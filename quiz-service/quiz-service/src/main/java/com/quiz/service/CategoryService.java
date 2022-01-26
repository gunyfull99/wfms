package com.quiz.service;

import com.quiz.Dto.CategoryEditRequest;
import com.quiz.Dto.CategoryRequest;
import com.quiz.entity.Category;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequest category);

    void editCategory(CategoryEditRequest category);

    List<CategoryRequest> getAllCategory();
    public List<Category> getAllCate();
}
