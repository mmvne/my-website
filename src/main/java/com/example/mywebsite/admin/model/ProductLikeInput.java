package com.example.mywebsite.admin.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString
@Data
public class ProductLikeInput {
    private Long productId;
    private String userId;

}
