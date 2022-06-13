package com.example.mywebsite.admin.controller;

import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AdminMainController {

    private final ProductRepository productRepository;

    @RequestMapping("/admin/main.do")
    public String main(Model model
            , @RequestParam(required = false, defaultValue = "") String searchText
            , @PageableDefault(size = 8, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Product> products =
                productRepository.findByKeywordContainingOrProductNameContainingOrContentsContaining(searchText, searchText, searchText, pageable);

        int startPage = 1;
        int endPage = products.getTotalPages();

        model.addAttribute("products", products);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/main";
    }

}
