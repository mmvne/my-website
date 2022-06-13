package com.example.mywebsite.user.service;

import com.example.mywebsite.user.dto.UserDto;
import com.example.mywebsite.user.model.ServiceResult;
import com.example.mywebsite.user.model.UserInput;
import com.example.mywebsite.user.model.UserPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

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

    /*
    * 회원 정보 가져오기
    * */
    UserDto detail(String id);

    /*
    * 회원 정보 수정
    * */
    ServiceResult updateUser(UserInput parameter);

    /*
     * 회원 탈퇴
     * */
    ServiceResult withdraw(UserPasswordInput parameter);

    /*
     * 회원 정보 비밀번호 변경
     * */
    ServiceResult password(UserPasswordInput parameter);

    /*
    * 관리자 회원 상태 변경
    * */
    boolean updateStatus(String userId, String userStatus);
}
