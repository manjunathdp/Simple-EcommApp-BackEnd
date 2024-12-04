package com.ecommerce.project.services;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategoryList() {
        var categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("Categories Not Added Yet!!!!!!!! ");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper
                        .map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already Exists");
        }
        Category categoryFromDB=categoryRepository.save(category);
        return modelMapper.map(categoryFromDB,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(long id) {
        List<Category> categoryList = categoryRepository.findAll();
        Category category = categoryList.stream()
                .filter(c -> c.getCategoryId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
        //return "Category with CategoryId: " + id + " is Deleted Successfully";


    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, long id) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        // Find the category to update
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already Exists");
        }

        // Update the category's fields
        existingCategory.setCategoryName(category.getCategoryName());

        // Save the updated category to the repository and return the CategoryDTO object
        return modelMapper.map(categoryRepository.save(existingCategory),CategoryDTO.class);

    }

}
