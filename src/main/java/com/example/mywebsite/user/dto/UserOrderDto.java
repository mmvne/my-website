package com.example.mywebsite.user.dto;

import com.example.mywebsite.user.entity.Cart;
import com.example.mywebsite.user.entity.UserOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserOrderDto {

    private Long orderId;

    private String userId;
    private Long productId;
    private LocalDateTime regDt;
    private String address1; //우편번호
    private String address2; //주소
    private String address3; //상세주소

    private String name; //수령자 이름
    private String phone; //수령자 폰

    private String urlFilename;
    private String productName;
    private String productOption;

    private Long amount;
    private Long price;
    private String orderRequest; //배송 요청사항
    private String orderName; //입금자명
    private String orderStatus;//처리 상태

    public static UserOrderDto of(UserOrder userOrder) {
        return UserOrderDto.builder()
                .orderId(userOrder.getOrderId())
                .userId(userOrder.getUserId())
                .productId(userOrder.getProductId())
                .regDt(userOrder.getRegDt())
                .address1(userOrder.getAddress1())
                .address2(userOrder.getAddress2())
                .address3(userOrder.getAddress3())
                .name(userOrder.getName())
                .phone(userOrder.getPhone())
                .productName(userOrder.getProductName())
                .productOption(userOrder.getProductOption())
                .urlFilename(userOrder.getUrlFilename())
                .amount(userOrder.getAmount())
                .price(userOrder.getPrice())
                .orderRequest(userOrder.getOrderRequest())
                .orderName(userOrder.getOrderName())
                .orderStatus(userOrder.getOrderStatus())
                .build();
    }

    public static List<UserOrderDto> of(List<UserOrder> UserOrders) {
        if (UserOrders == null) {
            return null;
        }
        List<UserOrderDto> userOrderDtoList = new ArrayList<>();
        for (UserOrder userOrder : UserOrders) {
            userOrderDtoList.add(of(userOrder));
        }
        return userOrderDtoList;
    }
}
