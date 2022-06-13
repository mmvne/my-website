package com.example.mywebsite.user.dto;

import com.example.mywebsite.user.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartDto {

    private Long cartId;
    private Long productId;
    private String productName;
    private String urlFilename;
    private String userId;
    private String option;
    private Long amount;
    private int price;
    private LocalDate regDt;

    public static CartDto of(Cart cart){
        return CartDto.builder()
                .cartId(cart.getCartId())
                .productId(cart.getProductId())
                .productName(cart.getProductName())
                .urlFilename(cart.getUrlFilename())
                .userId(cart.getUserId())
                .option(cart.getOption())
                .amount(cart.getAmount())
                .price(cart.getPrice())
                .regDt(cart.getRegDt())
                .build();
    }

    public static List<CartDto> of(List<Cart> carts) {
        if (carts == null) {
            return null;
        }
        List<CartDto> cartDtoList = new ArrayList<>();
        for (Cart cart : carts) {
            cartDtoList.add(of(cart));
        }
        return cartDtoList;
    }
}
