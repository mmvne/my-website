package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.admin.model.ProductLikeInput;
import com.example.mywebsite.user.dto.CartDto;
import com.example.mywebsite.user.model.ServiceResult;
import com.example.mywebsite.user.model.UserCartInput;

import java.util.List;

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

    /*
    * 상품 상세 보기
    * */
    ProductDto detail(long id);

    /*
    * 상품 좋아요 추가
    * */
    boolean addLike(ProductLikeInput parameter);

    /*
    * 상품 장바구니에 추가
    * */
    boolean addCart(UserCartInput parameter);

    /*
    * 장바구니 리스트
    * */
    List<CartDto> cartList(String userId);

    /*
    * 장바구니 선택상품 삭제
    * */
    boolean delCart(UserCartInput parameter);

    /*
    * 장바구니 수량, 가격 변경
    * */
    boolean cartUpdate(long id, long amount, int price, String userId);

    /*
    * 장바구니 - > 구매목록
    * */
    List<CartDto> cartOrderList(String[] cartIndex);

    /*
    * 관련 상품 리스트
    * */
    List<ProductDto> relatedList(Long categoryId);
}
