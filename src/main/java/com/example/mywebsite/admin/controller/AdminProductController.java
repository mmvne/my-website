package com.example.mywebsite.admin.controller;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.entity.Category;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.admin.repository.CategoryRepository;
import com.example.mywebsite.admin.service.CategoryService;
import com.example.mywebsite.admin.service.ProductService;
import com.example.mywebsite.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
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

    private final CategoryRepository categoryRepository;

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

        boolean result = productService.set(parameter);

        return "redirect:/admin/main.do";
    }

}
