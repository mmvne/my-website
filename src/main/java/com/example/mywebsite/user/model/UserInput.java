package com.example.mywebsite.user.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@ToString
@Data
public class UserInput {

    private String userId;
    private String userName;
    private String phone;
    private String password;

    // 우편번호 api를 위한 칼럼
    private String zipcode;
    private String addr;
    private String addrDetail;
}
