package com.example.mywebsite.user.service;

import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.user.entity.Review;
import com.example.mywebsite.user.model.ReviewInput;
import com.example.mywebsite.user.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public boolean add(ReviewInput parameter) {

        Optional<Product> optionalProduct = productRepository.findById(parameter.getProductId());

        if (optionalProduct.isEmpty()) {
            return false;
        }

        Product product = optionalProduct.get();

        product.setReviewScore(product.getReviewScore() + parameter.getReviewScore());
        product.setReviewCount(product.getReviewCount() + 1);

        productRepository.save(product);

        Review review = Review.builder()
                .productId(parameter.getProductId())
                .userId(parameter.getUserId())
                .filename(parameter.getFilename())
                .urlFilename(parameter.getUrlFilename())
                .reviewScore(parameter.getReviewScore())
                .text(parameter.getText())
                .productSize(parameter.getProductSize())
                .regDt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        return true;
    }

    @Override
    public boolean update(ReviewInput parameter) {

        Optional<Review> optionalReview = reviewRepository.findById(parameter.getReviewId());

        if (optionalReview.isEmpty()) {
            return false;
        }

        Optional<Product> optionalProduct = productRepository.findById(parameter.getProductId());

        if (optionalProduct.isEmpty()) {
            return false;
        }

        Product product = optionalProduct.get();
        Review review = optionalReview.get();

        product.setReviewScore(product.getReviewScore() - review.getReviewScore() + parameter.getReviewScore());

        productRepository.save(product);

        review.setFilename(parameter.getFilename());
        review.setUrlFilename(parameter.getUrlFilename());
        review.setReviewScore(parameter.getReviewScore());
        review.setText(parameter.getText());
        review.setUdtDt(LocalDateTime.now());

        reviewRepository.save(review);

        return true;
    }

    @Override
    public boolean delete(long id) {

        reviewRepository.deleteById(id);

        return true;
    }
}
