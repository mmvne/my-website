package com.example.mywebsite.user.model;

import lombok.Data;
import lombok.ToString;


@Data
public class ServiceResult {

    boolean result;
    String message;

    public ServiceResult(){
        result = true;
    }

    public ServiceResult(boolean result, String message){
        this.result = result;
        this.message = message;
    }
}
