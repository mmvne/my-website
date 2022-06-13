package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class ReplyInput {

    Long inquiryId;
    String replyText;

}
