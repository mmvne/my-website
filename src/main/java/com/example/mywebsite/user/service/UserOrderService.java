package com.example.mywebsite.user.service;

import com.example.mywebsite.user.dto.UserOrderDto;
import com.example.mywebsite.user.model.UserOrderInput;

import java.util.List;

public interface UserOrderService {

    /*
    * 구매하기 후 결과
    * */
    boolean orderResult(UserOrderInput parameter);

    /*
    * 구매 리스트
    * */
    List<UserOrderDto> orderList(String userId);
}
