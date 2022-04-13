package com.example.mywebsite.user.controller;

import com.example.mywebsite.user.model.UserInput;
import com.example.mywebsite.user.model.UserPasswordInput;
import com.example.mywebsite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @RequestMapping("/user/login")
    public String login(){

        return "user/login";
    }

    @GetMapping("/user/register")
    public String register(){

        return "user/register";
    }

    @PostMapping("/user/register")
    public String registerSubmit(UserInput parameter, Model model){

        boolean result = userService.register(parameter);

        model.addAttribute("result", result);

        return "user/register_complete";
    }

    @GetMapping("/user/email_auth")
    public String emailAuth(HttpServletRequest request, Model model){

        String uuid = request.getParameter("id");
        boolean result = userService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "user/email_auth";
    }

    @GetMapping("/user/find/password")
    public String findPassword(){

        return "user/find_password";
    }

    @PostMapping("/user/find/password")
    public String findPasswordSubmit(Model model, UserPasswordInput parameter){

        boolean result = false;

        try{
            result = userService.sendResetPassword(parameter);
        } catch(Exception e){
        }

        model.addAttribute("result", result);

        return "user/find_password_result";
    }

    @GetMapping("/user/reset/password")
    public String resetPassword(Model model, HttpServletRequest request){

        String uuid = request.getParameter("id");

        boolean result = userService.checkResetPassword(uuid);

        model.addAttribute("result", result);

        return "user/reset_password";
    }

    @PostMapping("/user/reset/password")
    public String resetPasswordSubmit(Model model, UserPasswordInput parameter){

        boolean result = false;

        try{
            result = userService.resetPassword(parameter.getId(), parameter.getPassword());
        }catch (Exception e){
        }
        model.addAttribute("result", result);

        return "user/reset_password_result";
    }


    @GetMapping("/user/cart")
    public String cart(){

        return "user/cart";
    }
}
