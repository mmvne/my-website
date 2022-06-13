package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.model.OrderStatusInput;

public interface AdminOrderService {

    /*
    *  주문 상태 변경
    * */
    boolean updateStatus(OrderStatusInput parameter);
}
