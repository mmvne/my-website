package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.CategoryDto;
import com.example.mywebsite.admin.entity.Category;
import com.example.mywebsite.admin.model.CategoryInput;
import com.example.mywebsite.admin.repository.CategoryRepository;
import com.example.mywebsite.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ServiceResult add(CategoryInput parameter) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(parameter.getCategoryName());
        if (optionalCategory.isPresent()) {
            return new ServiceResult(false, "이미 존재하는 카테고리 입니다.");
        }

        Category category = Category.builder()
                .categoryName(parameter.getCategoryName())
                .build();
        categoryRepository.save(category);

        return new ServiceResult();
    }

    @Override
    public List<CategoryDto> list() {

        List<Category> categories = categoryRepository.findAll();

        return CategoryDto.of(categories);
    }

    @Override
    public boolean delete(CategoryInput parameter) {
        categoryRepository.deleteById(parameter.getId());
        return true;
    }
}
