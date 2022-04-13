package com.example.mywebsite.user.repository;

import com.example.mywebsite.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmailAuthKey(String uuid);
    Optional<User> findByResetPasswordKey(String uuid);
}
