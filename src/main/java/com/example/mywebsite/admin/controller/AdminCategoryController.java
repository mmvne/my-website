package com.example.mywebsite.admin.controller;

import com.example.mywebsite.admin.dto.CategoryDto;
import com.example.mywebsite.admin.model.CategoryInput;
import com.example.mywebsite.admin.service.CategoryService;
import com.example.mywebsite.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/category/add.do")
    public String categoryAdd(CategoryInput parameter, Model model){

        ServiceResult result = categoryService.add(parameter);
        if(!result.isResult()){
            model.addAttribute("message",result.getMessage());
            return "common/error";
        }

        return "redirect:/admin/category/list.do";
    }

    @GetMapping("/admin/category/list.do")
    public String categoryList(Model model){

        List<CategoryDto> list = categoryService.list();

        model.addAttribute("list", list);
        return "admin/category/list";
    }

    @PostMapping("admin/category/delete.do")
    public String categoryDel(CategoryInput parameter){

        boolean result = categoryService.delete(parameter);

        return "redirect:/admin/category/list.do";
    }

}
