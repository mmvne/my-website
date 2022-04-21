package com.example.mywebsite.admin.dto;

import com.example.mywebsite.admin.entity.Product;
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
public class ProductDto {
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
    private Long likeCount; // 좋아요수
    private Long saleCount; // 누적 판매량

    private String filename;
    private String urlFilename;

    public static ProductDto of(Product product){
        return ProductDto.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategoryId())
                .keyword(product.getKeyword())
                .productName(product.getProductName())
                .regDt(product.getRegDt())
                .udtDt(product.getUdtDt())
                .gender(product.getGender())
                .contents(product.getContents())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .saleEndDt(product.getSaleEndDt())
                .saleYn(product.isSaleYn())
                .sizeLarge(product.getSizeLarge())
                .sizeXlarge(product.getSizeXlarge())
                .sizeFree(product.getSizeFree())
                .likeCount(product.getLikeCount())
                .saleCount(product.getSaleCount())
                .filename(product.getFilename())
                .urlFilename(product.getUrlFilename())
                .build();
    }

    public static List<ProductDto> of(List<Product> products) {
        if (products == null) {
            return null;
        }
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            productDtoList.add(of(product));
        }
        return productDtoList;
    }
}
