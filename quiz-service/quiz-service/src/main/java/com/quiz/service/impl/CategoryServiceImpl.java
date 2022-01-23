package com.quiz.service.impl;

import com.quiz.Dto.CategoryEditRequest;
import com.quiz.Dto.CategoryRequest;
import com.quiz.entity.Category;
import com.quiz.repository.CategoryRepository;
import com.quiz.repository.QuestionRepository;
import com.quiz.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryRequest category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            throw new RuntimeException("this category was existed !!!");
        }
        Category categoryEntity = new Category();
        categoryEntity.setName(category.getName());
        categoryEntity.setActive(category.isActive());
        categoryRepository.save(categoryEntity);
    }


    @Override
    public void editCategory(CategoryEditRequest request) {
        Category categoryEntity = categoryRepository.getById(request.getId());
        if (categoryEntity == null) {
            throw new RuntimeException("this category not exist!!!");
        }
        categoryEntity.setName(request.getName());
        categoryEntity.setActive(request.isActive());
        categoryRepository.save(categoryEntity);
    }

    @Override
    public List<CategoryRequest> getAllCategory() {
        List<Category> categoryEntities = categoryRepository.findAll();
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        for (Category categoryEntity : categoryEntities){
            CategoryRequest categoryRequest = new CategoryRequest();
            categoryRequest.setName(categoryEntity.getName());
            categoryRequest.setActive(categoryEntity.isActive());
            categoryRequests.add(categoryRequest);
        }
        return categoryRequests;
    }


}
