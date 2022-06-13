package com.example.mywebsite.user.repository;

import com.example.mywebsite.user.entity.UserOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findAllByUserId(String userId, Sort sort);

    List<UserOrder> findAllByRegDtBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    Page<UserOrder> findByNameContainingOrOrderNameContainingOrPhoneContainingOrProductNameContaining
            (String name, String orderName, String phone, String productName, Pageable pageable);

}