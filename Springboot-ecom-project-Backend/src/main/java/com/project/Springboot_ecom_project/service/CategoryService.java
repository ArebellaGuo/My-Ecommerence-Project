package com.project.Springboot_ecom_project.service;

import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO category);

    CategoryDTO updateCategoryById(CategoryDTO category, Long categoryId);

    String deleteCategoryById(Long categoryId);
}
