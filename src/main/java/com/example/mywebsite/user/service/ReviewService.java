package com.example.mywebsite.user.service;

import com.example.mywebsite.user.model.ReviewInput;

public interface ReviewService {

    /*
    * 리뷰 추가
    * */
    boolean add(ReviewInput parameter);
    /*
    * 리뷰 수정
    * */
    boolean update(ReviewInput parameter);

    /*
    * 리뷰 삭제
    * */
    boolean delete(long id);
}
