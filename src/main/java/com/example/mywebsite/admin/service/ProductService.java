package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.user.model.ServiceResult;

public interface ProductService {

    /*
    * 상품 추가
    * */
    boolean add(ProductInput parameter);

    /*
    * 상품 삭제
    * */
    boolean delete(long id);

    /*
    * 상품 정보 (수정)
    * */
    ProductDto update(long id);

    /*
    * 상품 수정
    * */
    boolean set(ProductInput parameter);
}
