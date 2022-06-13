package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class UserCartInput {

    private Long cartId;

    private Long productId;
    private String userId;
    private String productName;
    private String urlFilename;
    private String option;
    private int price;
    private Long amount;
}
