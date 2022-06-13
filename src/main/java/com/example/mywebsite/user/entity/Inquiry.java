package com.example.mywebsite.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    private String userId;
    private Long orderId;
    private String phone;
    private String name;

    private String inquiryType;
    private String subject;
    private String inquiryText;
    private String replyText;

    private String filename;
    private String urlFilename;

    private LocalDateTime regDt;
}
