package com.example.mywebsite.user.entity;

public interface OrderCode {

    //입금 확인
    String ORDER_STATUS_CHECK = "CHECK";

    //출고 대기
    String ORDER_STATUS_STANDBY = "STANDBY";

    //출고 완료
    String ORDER_STATUS_COMPLETED = "COMPLETED";


}
