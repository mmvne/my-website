package com.example.mywebsite.user.repository;

import com.example.mywebsite.user.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findAllByUserId(String userId, Sort sort);

    Page<Inquiry> findByUserIdContaining(String userId, Pageable pageable);

}
