package com.example.mywebsite.user.dto;

import com.example.mywebsite.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    @Id
    private String userId;

    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;//회원정보 수정일

    private boolean emailAuthYn;
    private LocalDateTime emailAuthDt;
    private String emailAuthKey;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYn;

    private String userStatus;//이용가능, 정지

    // 우편번호 api를 위한 칼럼
    private String zipcode;
    private String addr;
    private String addrDetail;

    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .password(user.getPassword())
                .regDt(user.getRegDt())
                .udtDt(user.getUdtDt())
                .emailAuthYn(user.isEmailAuthYn())
                .emailAuthDt(user.getEmailAuthDt())
                .emailAuthKey(user.getEmailAuthKey())
                .resetPasswordKey(user.getResetPasswordKey())
                .resetPasswordLimitDt(user.getResetPasswordLimitDt())
                .adminYn(user.isAdminYn())
                .userStatus(user.getUserStatus())
                .zipcode(user.getZipcode())
                .addr(user.getAddr())
                .addrDetail(user.getAddrDetail())
                .build();
    }
}
