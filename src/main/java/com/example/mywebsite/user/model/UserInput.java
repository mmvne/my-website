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
}
