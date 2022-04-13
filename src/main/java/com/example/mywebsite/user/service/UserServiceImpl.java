package com.example.mywebsite.user.service;

import com.example.mywebsite.components.MailComponents;
import com.example.mywebsite.exception.UserNotEmailAuthException;
import com.example.mywebsite.exception.UserStopUserException;
import com.example.mywebsite.user.entity.User;
import com.example.mywebsite.user.model.UserInput;
import com.example.mywebsite.user.model.UserPasswordInput;
import com.example.mywebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.mywebsite.user.entity.UserCode.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final MailComponents mailComponents;

    @Override
    public boolean register(UserInput parameter) {

        Optional<User> optionalUser = userRepository.findById(parameter.getUserId());
        // 이미 회원으로 존재하다면 false
        if (optionalUser.isPresent()) {
            return false;
        }

        // 비밀번호 암호화 해서 저장
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        // 이메일 인증을 위한 랜덤 uuid 키 발급
        String uuid = UUID.randomUUID().toString();

        // 회원가입
        User user = User.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .password(encPassword)
                .phone(parameter.getPhone())
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .userStatus(USER_STATUS_REQ)
                .build();
        // db 에 저장
        userRepository.save(user);

        //이메일 보내기
        String email = parameter.getUserId();
        String subject = "사이트 가입을 축하드립니다.";
        String text = "<p>사이트 가입을 축하드립니다.</p><p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/user/email_auth?id=" + uuid + "'>가입 완료</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {

        Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        user.setEmailAuthYn(true);
        user.setEmailAuthDt(LocalDateTime.now());
        user.setUserStatus(USER_STATUS_ING);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean sendResetPassword(UserPasswordInput parameter) {
        Optional<User> optionalUser = userRepository.findById(parameter.getUserId());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        String uuid = UUID.randomUUID().toString();
        System.out.println("SendReset UUID : " + uuid);
        user.setResetPasswordKey(uuid);
        user.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        userRepository.save(user);

        String email = user.getUserId();
        String subject = "비밀번호 초기화 메일 입니다.";
        String text = "<p>비밀번호 초기화 메일 입니다.</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/user/reset/password?id=" + uuid + "'>비밀번화 초기화 링크</a></div>";

        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {

        System.out.println("UUID : " + uuid);
        Optional<User> optionalUser = userRepository.findByResetPasswordKey(uuid);
        System.out.println("Optional : " + optionalUser.toString());
        if(optionalUser.isEmpty()){
            return false;
        }

        User user = optionalUser.get();

        if (user.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if (user.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        return true;
    }

    @Override
    public boolean resetPassword(String id, String password) {

        Optional<User> optionalUser = userRepository.findByResetPasswordKey(id);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        if (user.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if (user.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(encPassword);
        user.setResetPasswordKey("");
        user.setResetPasswordLimitDt(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findById(username);

        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        if(USER_STATUS_REQ.equals(user.getUserStatus())){
            throw new UserNotEmailAuthException("이메일 인증 후 사용해 주세요.");
        }

        if(USER_STATUS_STOP.equals(user.getUserStatus())){
            throw new UserStopUserException("정지된 회원입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (user.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), grantedAuthorities);
    }
}
