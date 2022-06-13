package com.example.mywebsite.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDate saleEndDt;

    private Long sizeLarge; //사이즈별 수량
    private Long sizeXlarge;
    private Long sizeFree;
    private Long saleCount; // 누적 판매량

    private Long reviewScore;
    private Long reviewCount;

    private String filename;
    private String urlFilename;
}
