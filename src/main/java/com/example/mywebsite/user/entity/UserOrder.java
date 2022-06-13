package com.example.mywebsite.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
