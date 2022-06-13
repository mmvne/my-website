package com.example.mywebsite.admin.controller;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.entity.Category;
import com.example.mywebsite.admin.model.OrderStatusInput;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.admin.repository.CategoryRepository;
import com.example.mywebsite.admin.service.AdminOrderService;
import com.example.mywebsite.admin.service.CategoryService;
import com.example.mywebsite.admin.service.ProductService;
import com.example.mywebsite.user.entity.Inquiry;
import com.example.mywebsite.user.entity.User;
import com.example.mywebsite.user.entity.UserOrder;
import com.example.mywebsite.user.model.ReplyInput;
import com.example.mywebsite.user.repository.InquiryRepository;
import com.example.mywebsite.user.repository.UserOrderRepository;
import com.example.mywebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class AdminProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AdminOrderService adminOrderService;


    private final CategoryRepository categoryRepository;
    private final UserOrderRepository userOrderRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename) {

        LocalDate now = LocalDate.now();

        String[] dirs = {
                String.format("%s/%d/", baseLocalPath,now.getYear()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(),now.getMonthValue()),
                String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())};

        String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for(String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }

        String fileExtension = "";
        if (originalFilename != null) {
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1) {
                fileExtension = originalFilename.substring(dotPos + 1);
            }
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", ""); // -로 생성시 없애주기
        String newFilename = String.format("%s%s", dirs[2], uuid);
        String newUrlFilename = String.format("%s%s", urlDir, uuid);
        if (fileExtension.length() > 0) {
            newFilename += "." + fileExtension;
            newUrlFilename += "." + fileExtension;
        }

        return new String[]{newFilename, newUrlFilename};
    }

    @GetMapping("/admin/product/add.do")
    public String productAdd(Model model){

        model.addAttribute("category", categoryService.list());

        return "admin/product/add";
    }

    @PostMapping("/admin/product/add.do")
    public String productAddSubmit(Model model, ProductInput parameter
                        , MultipartFile file){

        System.out.println("Parameter : " + parameter.toString());

        String saveFilename = "";
        String urlFilename = "";

        if (file != null) {
            String originalFilename = file.getOriginalFilename();

            String baseLocalPath = "C:/Users/user/IdeaProjects/mywebsite/files";
            String baseUrlPath = "/files";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
            }
        }

        parameter.setFilename(saveFilename);
        parameter.setUrlFilename(urlFilename);

        boolean result = productService.add(parameter);

        return "redirect:/admin/main.do";
    }


    @GetMapping("/admin/product/delete.do")
    public String productDel(@RequestParam("id") long id){

        boolean result = productService.delete(id);

        return "redirect:/admin/main.do";
    }

    @GetMapping("/admin/product/update.do")
    public String productUpdate(Model model, @RequestParam("id") long id){

        ProductDto detail = productService.update(id);

        Optional<Category> optionalCategory =  categoryRepository.findById(detail.getCategoryId());
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            model.addAttribute("categoryName", category.getCategoryName());
        }

        model.addAttribute("category", categoryService.list());
        model.addAttribute("detail", detail);

        return "admin/product/update";
    }

    @PostMapping("/admin/product/update.do")
    public String productUdtSubmit(Model model, ProductInput parameter
            , MultipartFile file){


        String saveFilename = "";
        String urlFilename = "";

        if (file != null) {
            String originalFilename = file.getOriginalFilename();

            String baseLocalPath = "C:/Users/user/IdeaProjects/mywebsite/files";
            String baseUrlPath = "/files";

            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
            }
        }

        parameter.setFilename(saveFilename);
        parameter.setUrlFilename(urlFilename);

        boolean result = productService.set(parameter);

        return "redirect:/admin/main.do";
    }

    @GetMapping("/admin/order/list.do")
    public String orderListAdmin(Model model
            , @RequestParam(required = false, defaultValue = "") String searchText
            , @PageableDefault(size = 10, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<UserOrder> list = userOrderRepository
                .findByNameContainingOrOrderNameContainingOrPhoneContainingOrProductNameContaining
                        (searchText, searchText, searchText, searchText, pageable);

        int startPage = 1;
        int endPage = list.getTotalPages();

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/order/list";
    }

    @PostMapping("/admin/status/update.do")
    public String productAddCart(OrderStatusInput parameter){

        System.out.println("==================================");
        System.out.println(parameter.toString());
        System.out.println("==================================");
        boolean result = adminOrderService.updateStatus(parameter);

        return "redirect:/admin/order/list.do";
    }

    @GetMapping("/admin/inquiry/list.do")
    public String adminInquiryList(Model model
            , @RequestParam(required = false, defaultValue = "") String searchText
            , @PageableDefault(size = 10, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Inquiry> list = inquiryRepository.findByUserIdContaining(searchText, pageable);

        int startPage = 1;
        int endPage = list.getTotalPages();

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/product/inquiryList";
    }

    @GetMapping("/admin/inquiry/check")
    public String adminInquiryDetail(@RequestParam("id") long id, Model model){

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);

        if (optionalInquiry.isEmpty()) {
            return null;
        }

        Inquiry inquiry = optionalInquiry.get();
        if(inquiry.getUrlFilename().contains("jpg")){
            model.addAttribute("image", inquiry.getUrlFilename());
        }else{
            model.addAttribute("image", null);
        }

        Optional<UserOrder> optionalUserOrder = userOrderRepository.findById(inquiry.getOrderId());

        if (optionalUserOrder.isEmpty()) {
            return null;
        }

        UserOrder userOrder = optionalUserOrder.get();

        Optional<User> optionalUser = userRepository.findById(inquiry.getUserId());

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("userOrder", userOrder);
        model.addAttribute("user", user);

        return "admin/product/inquiryDetail";
    }

    @GetMapping("/admin/inquiry/reply.do")
    public String adminInquiryReply(Model model, @RequestParam("id") long id){

        model.addAttribute("id", id);

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);

        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            model.addAttribute("inquiry", inquiry);
        }

        return "admin/product/reply";
    }

    @PostMapping("/admin/inquiry/reply.do")
    public String adminInquiryReplyAdd(ReplyInput parameter){

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(parameter.getInquiryId());

        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setReplyText(parameter.getReplyText());
            inquiryRepository.save(inquiry);
        }

        return "redirect:/admin/inquiry/list.do";
    }

}
