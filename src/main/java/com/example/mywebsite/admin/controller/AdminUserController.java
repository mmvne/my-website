package com.example.mywebsite.admin.controller;

import com.example.mywebsite.user.dto.UserDto;
import com.example.mywebsite.user.entity.User;
import com.example.mywebsite.user.model.ServiceResult;
import com.example.mywebsite.user.model.UserStatusInput;
import com.example.mywebsite.user.repository.UserRepository;
import com.example.mywebsite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class AdminUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/admin/user/list.do")
    public String userList(Model model
            , @RequestParam(required = false, defaultValue = "") String searchText
            , @PageableDefault(size = 10, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<User> list = userRepository.findByUserIdContainingOrUserNameContaining(searchText, searchText,  pageable);

        int startPage = 1;
        int endPage = list.getTotalPages();

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/user/list";
    }

    @GetMapping("/admin/user/detail.do")
    public String userDetail(Model model, HttpServletRequest request) {

        String userId = request.getParameter("id");

        UserDto detail = userService.detail(userId);

        model.addAttribute("detail", detail);

        return "admin/user/detail";
    }

    @PostMapping("/admin/user/status.do")
    public String userStatus(Model model, UserStatusInput parameter) {

        boolean result = userService.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        System.out.println(parameter.toString());

        return "redirect:/admin/user/detail.do?id=" + parameter.getUserId();
    }


}
