package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString
@Data
public class ReviewInput {
    private Long reviewId;

    private Long productId;

    private String userId;

    private int reviewScore;
    private String filename;
    private String urlFilename;
    private String text;
    private String productSize;

    private LocalDateTime regDt;
}
