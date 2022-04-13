package com.example.mywebsite.user.service;

import com.example.mywebsite.user.model.UserInput;
import com.example.mywebsite.user.model.UserPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

public interface UserService extends UserDetailsService {
    /*
    * 회원가입
    * */
    boolean register(UserInput parameter);

    /*
     * 이메일 인증
     * */
    boolean emailAuth(String uuid);

    /*
    * 비밀번호 초기화 링크 보내기
    * */
    boolean sendResetPassword(UserPasswordInput parameter);


    /*
    * 초기화 코드, 날짜 유효한지 확인
   * */
    boolean checkResetPassword(String uuid);


    /*
    * uuid와 password 가져와서 비밀번호 변경
    * */
    boolean resetPassword(String id, String password);
}
