package com.project.Springboot_ecom_project.service.impl;

import com.project.Springboot_ecom_project.exception.APIException;
import com.project.Springboot_ecom_project.exception.ResourceNotFound;
import com.project.Springboot_ecom_project.model.Category;
import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.CategoryResponse;
import com.project.Springboot_ecom_project.repository.CategoryRepository;
import com.project.Springboot_ecom_project.service.CategoryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //configure sort details
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //configure page details
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categoryList = categoryPage.getContent();

        //validate if categoryList is empty
        if (categoryList.isEmpty()){
            throw new APIException("Category is empty now!");
        }
        //convert categoryList into categoryDTOList
        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .collect(Collectors.toList());
        //add page details and categoryDTOList to categoryResponse
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        String categoryName = categoryDTO.getCategoryName();
        Category categoryFromDB = categoryRepository.findByCategoryName(categoryName);
        if (categoryFromDB != null){
            throw new APIException("Category already exists!");
        }
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategoryById(CategoryDTO categoryDTO, Long categoryId) {
        //validate if category exists by categoryId
        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFound("Category",categoryId));

        if (categoryRepository.findByCategoryName(categoryDTO.getCategoryName()) != null &&
                !categoryFromDB.getCategoryName().equals(categoryDTO.getCategoryName())) {
            throw new APIException("Category name already exists!");
        }
        categoryFromDB.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(categoryFromDB);
        return modelMapper.map(updatedCategory,CategoryDTO.class);
    }

    @Override
    @Transactional
    public String deleteCategoryById(Long categoryId) {
        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFound("Category",categoryId));

        categoryRepository.deleteById(categoryId);
        return "Category is deleted successfully with categoryId: " +categoryId ;
    }


}
