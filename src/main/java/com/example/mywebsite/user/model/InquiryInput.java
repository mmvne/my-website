package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString
@Data
public class InquiryInput {

    String userId;
    Long orderId;
    String inquiryType;
    String subject;
    String inquiryText;
    String phone;
    String name;

    String filename;
    String UrlFilename;
}
