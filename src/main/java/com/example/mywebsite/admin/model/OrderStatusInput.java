package com.example.mywebsite.admin.model;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class OrderStatusInput {

    private Long orderId;
    private String orderStatus;
}
