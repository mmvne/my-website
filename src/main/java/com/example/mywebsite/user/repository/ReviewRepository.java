package com.example.mywebsite.user.repository;

import com.example.mywebsite.user.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);

    Page<Review> findAllByProductId(Long productId, Pageable pageable);

}
