package com.project.Springboot_ecom_project.controller;

import com.project.Springboot_ecom_project.config.PageInstants;
import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.CategoryResponse;
import com.project.Springboot_ecom_project.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * user, seller  -> get all categories
 * admin -> delete, update categories
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = PageInstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PageInstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy" ,defaultValue = PageInstants.SORT_CATEGORY_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = PageInstants.SORT_ORDER,required = false) String sortOrder
    ){
        CategoryResponse categoryList = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){;
        CategoryDTO newCategoryDTO = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(newCategoryDTO,HttpStatus.CREATED);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId){
        CategoryDTO updatedCategoryDTO= categoryService.updateCategoryById(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        String status = categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
