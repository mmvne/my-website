package com.example.mywebsite.admin.repository;

import com.example.mywebsite.admin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
