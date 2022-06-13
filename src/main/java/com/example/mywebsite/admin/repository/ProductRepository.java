package com.example.mywebsite.admin.repository;

import com.example.mywebsite.admin.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByKeywordContainingOrProductNameContainingOrContentsContaining(String keyword, String productName, String contents, Pageable pageable);

    List<Product> findAllByCategoryId(Long categoryId);

}
