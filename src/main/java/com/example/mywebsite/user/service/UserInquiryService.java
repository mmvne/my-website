package com.example.mywebsite.user.service;

import com.example.mywebsite.user.model.InquiryInput;

public interface UserInquiryService {

    /*
    * 문의 등록
    * */
    boolean add(InquiryInput parameter);
}
