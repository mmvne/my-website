package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class UserPasswordInput {

    private String userId;

    // 초괴화용 uuid
    private String id;
    private String password;
}
