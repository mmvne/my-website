package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;


@ToString
@Data
public class UserOrderInput {

    String userId;
    String productIndexList;
    String productPriceList;
    String productAmountList;
    String productUrlFileList;
    String productNameList;
    String productOptionList;
    String postNo2;
    String address3;
    String address4;
    String orderName2;
    String firstCellPhoneNum2;
    String middleCellPhoneNum2;
    String lastCellPhoneNum2;
    String sendMessage;
    String sendName;

}
