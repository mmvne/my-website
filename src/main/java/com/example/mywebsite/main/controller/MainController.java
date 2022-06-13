package com.example.mywebsite.main.controller;

import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.user.entity.Review;
import com.example.mywebsite.user.repository.CartRepository;
import com.example.mywebsite.user.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;

    @RequestMapping("/")
    public String index(Model model
            , @RequestParam(required = false, defaultValue = "") String searchText
            , @PageableDefault(size = 8, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable
            , Principal principal) {

//        Page<Product> list = productRepository.findAll(pageable);
        Page<Product> list =
                productRepository.findByKeywordContainingOrProductNameContainingOrContentsContaining(searchText, searchText, searchText, pageable);
        int startPage = 1;
        int endPage = list.getTotalPages();

        Long cartCount;
        if(principal == null){
            cartCount = 0L;
        }else{
            String userId = principal.getName();
            cartCount = cartRepository.countByUserId(userId);
        }

        for(Product p : list){
            if (p.getSaleEndDt() != null) {
                if (LocalDate.now().isAfter(p.getSaleEndDt())) {
                    p.setSaleYn(false);
                    productRepository.save(p);
                }
            }
        }

        model.addAttribute("cartCount", cartCount);
        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "index";
    }
}
