package com.example.mywebsite.user.repository;

import com.example.mywebsite.user.entity.Cart;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Long countByUserId(String userId);
    List<Cart> findAllCartByUserId(String userId, Sort sort);

}
