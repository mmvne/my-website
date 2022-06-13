package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class UserStatusInput {

    private String userId;
    private String userStatus;
}
