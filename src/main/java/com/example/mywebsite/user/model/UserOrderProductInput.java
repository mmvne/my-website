package com.example.mywebsite.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Data
public class UserOrderProductInput {

    LocalDate redDt;
    String urlFilename;
    String productName;
    String productOption;
    Long amount;
    Long price;
}
