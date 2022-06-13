package com.example.mywebsite.user.service;

import com.example.mywebsite.user.entity.Inquiry;
import com.example.mywebsite.user.model.InquiryInput;
import com.example.mywebsite.user.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserInquiryServiceImpl implements UserInquiryService {

    private final InquiryRepository inquiryRepository;


    @Override
    public boolean add(InquiryInput parameter) {

        Inquiry inquiry = Inquiry.builder()
                .userId(parameter.getUserId())
                .orderId(parameter.getOrderId())
                .inquiryType(parameter.getInquiryType())
                .subject(parameter.getSubject())
                .inquiryText(parameter.getInquiryText())
                .replyText(null)
                .phone(parameter.getPhone())
                .name(parameter.getName())
                .filename(parameter.getFilename())
                .urlFilename(parameter.getUrlFilename())
                .regDt(LocalDateTime.now())
                .build();

        inquiryRepository.save(inquiry);

        return true;
    }
}
