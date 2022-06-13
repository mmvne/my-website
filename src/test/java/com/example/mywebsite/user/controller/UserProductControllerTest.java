package com.example.mywebsite.user.controller;

import com.example.mywebsite.admin.repository.ProductLikeRepository;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.admin.service.ProductService;
import com.example.mywebsite.user.repository.*;
import com.example.mywebsite.user.service.ReviewService;
import com.example.mywebsite.user.service.UserInquiryService;
import com.example.mywebsite.user.service.UserOrderService;
import com.example.mywebsite.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserProductControllerTest {

    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    UserInquiryService userInquiryService;

    @Autowired
    ProductLikeRepository productLikeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InquiryRepository inquiryRepository;


    @Test
    @Transactional
    @Rollback
    void productDetail() {
    }

    @Test
    void productLike() {
    }

    @Test
    void productCart() {
    }

    @Test
    void productCartDel() {
    }

    @Test
    void productAddCart() {
    }

    @Test
    void testProductAddCart() {
    }

    @Test
    void userCartOrder() {
    }

    @Test
    void userDetailOrder() {
    }

    @Test
    void userOrderResult() {
    }

    @Test
    void userOrderList() {
    }

    @Test
    void productReview() {
    }

    @Test
    void productReviewAddSubmit() {
    }

    @Test
    void productReviewUpdate() {
    }

    @Test
    void productReviewUpdateSubmit() {
    }

    @Test
    void productReviewDelete() {
    }

    @Test
    void productInquiry() {
    }

    @Test
    void testProductInquiry() {
    }

    @Test
    void productAddInquiry() {
    }

    @Test
    void inquiryCheck() {
    }
}