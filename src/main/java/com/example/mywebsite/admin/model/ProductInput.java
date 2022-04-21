package com.example.mywebsite.admin.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;


@ToString
@Data
public class ProductInput {
    private Long productId;

    private Long categoryId;

    private String keyword;
    private String productName;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;
    private String gender;
    private String contents; //상세내용

    private long price;
    private long salePrice;
    private boolean saleYn;
    String saleEndDtText;

    private Long sizeLarge; //사이즈별 수량
    private Long sizeXlarge;
    private Long sizeFree;
    private Long likeCount; // 좋아요수
    private Long saleCount; // 누적 판매량

    private String filename;
    private String urlFilename;

}
