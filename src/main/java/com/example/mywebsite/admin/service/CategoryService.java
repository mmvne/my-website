package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.CategoryDto;
import com.example.mywebsite.admin.model.CategoryInput;
import com.example.mywebsite.user.model.ServiceResult;

import java.util.List;

public interface CategoryService {
    /*
    * 카테고리 추가
    * */
    ServiceResult add(CategoryInput parameter);

    /*
    * 카테고리 리스트
    * */
    List<CategoryDto> list();

    /*
    * 카테고리 삭제
    * */
    boolean delete(CategoryInput parameter);
}
