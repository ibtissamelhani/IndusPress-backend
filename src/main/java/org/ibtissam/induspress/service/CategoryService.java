package org.ibtissam.induspress.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.exception.CategoryNotFoundException;
import org.ibtissam.induspress.model.Category;
import org.ibtissam.induspress.repository.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;



    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new DataIntegrityViolationException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepository.save(new Category(category.getName()));
    }


    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category updateCategory(UUID id, Category request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new DataIntegrityViolationException("Category with name '" + request.getName() + "' already exists");
        }

        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }


}
