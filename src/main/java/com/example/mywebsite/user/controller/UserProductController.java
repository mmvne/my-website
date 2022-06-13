package com.example.mywebsite.user.controller;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.entity.ProductLike;
import com.example.mywebsite.admin.model.ProductLikeInput;
import com.example.mywebsite.admin.repository.ProductLikeRepository;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.admin.service.ProductService;
import com.example.mywebsite.user.entity.Inquiry;
import com.example.mywebsite.user.entity.Review;
import com.example.mywebsite.user.entity.User;
import com.example.mywebsite.user.model.*;
import com.example.mywebsite.user.repository.*;
import com.example.mywebsite.user.service.ReviewService;
import com.example.mywebsite.commom.model.ResponseResult;
import com.example.mywebsite.user.dto.CartDto;
import com.example.mywebsite.user.dto.UserDto;
import com.example.mywebsite.user.dto.UserOrderDto;
import com.example.mywebsite.user.entity.UserOrder;
import com.example.mywebsite.user.service.UserInquiryService;
import com.example.mywebsite.user.service.UserOrderService;
import com.example.mywebsite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Long.parseLong;

@RequiredArgsConstructor
@Controller
public class UserProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final UserOrderService userOrderService;
    private final UserInquiryService userInquiryService;

    private final ProductLikeRepository productLikeRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserOrderRepository userOrderRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

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

    @GetMapping("/user/product/detail")
    public String productDetail(Model model, @RequestParam("id") long id
                                , @PageableDefault(size = 5, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable
                                , Principal principal){

        ProductDto detail = productService.detail(id);
        Long categoryId = detail.getCategoryId();
        List<ProductDto> list = productService.relatedList(categoryId);

        if(list.size() < 4){
            model.addAttribute("list", list);
        }else{
            List<ProductDto> relatedList = new ArrayList<>();
            relatedList.add(list.get(0));
            relatedList.add(list.get(1));
            relatedList.add(list.get(2));
            relatedList.add(list.get(3));
            model.addAttribute("list", relatedList);
        }

        Page<Review> reviewList = reviewRepository.findAllByProductId(id, pageable);

        int startPage = 1;
        int endPage = reviewList.getTotalPages();

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        int likeCount = productLikeRepository.countByProductId(id);
        Long cartCount;
        boolean result;

        if (principal == null) {
            cartCount = 0L;
            model.addAttribute("likeCount", likeCount);
            model.addAttribute("detail", detail);
            model.addAttribute("cartCount", cartCount);
        }else{
            Optional<ProductLike> optionalProductLike
                    = productLikeRepository.findByUserIdAndProductId(principal.getName(), id);
            String userId = principal.getName();
            cartCount = cartRepository.countByUserId(userId);
            if(optionalProductLike.isEmpty()){
                result = false;
            } else {
                result = true;
            }


            model.addAttribute("cartCount", cartCount);
            model.addAttribute("result", result);
            model.addAttribute("likeCount", likeCount);
            model.addAttribute("detail", detail);
        }

        return "user/product/detail";
    }

    @PostMapping("/user/product/like")
    public String productLike(ProductLikeInput parameter, Principal principal){

        parameter.setUserId(principal.getName());

        boolean result = productService.addLike(parameter);

        return "redirect:/user/product/detail?id=" + parameter.getProductId();
    }

    @GetMapping("/user/product/cart")
    public String productCart(Model model, Principal principal){
        String userId = principal.getName();
        List<CartDto> cartlist = productService.cartList(userId);

        model.addAttribute("cartList", cartlist);

        return "user/product/cart";
    }

    @PostMapping("/user/cart/delete.do")
    public String productCartDel(UserCartInput parameter){

        System.out.println(parameter.toString());

        boolean result = productService.delCart(parameter);

        return "redirect:/user/product/cart";
    }

    @PostMapping("/user/product/addCart")
    public String productAddCart(UserCartInput parameter, Principal principal){

        parameter.setUserId(principal.getName());

        boolean result = productService.addCart(parameter);

        return "redirect:/user/product/cart";
    }

    @PatchMapping("/user/cart/update")
    public ResponseEntity<?> productAddCart( @RequestParam("cartId") long id,
                                             @RequestParam("amount") long amount,
                                             @RequestParam("price") int price, Principal principal){

        String userId = principal.getName();
        System.out.println("id = " + id + " amount = " + amount + " price =" + price);
        boolean result = productService.cartUpdate(id, amount, price, userId);

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }

    @GetMapping("/user/cart/order")
    public String userCartOrder(@RequestParam("cartList") String cartList, Principal principal, Model model){

        if(principal != null){
            UserDto user = userService.detail(principal.getName());
            String[] phone = user.getPhone().split("-");
            String[] email = user.getUserId().split("@");
            String middleNum = phone[1];
            String lastNum = phone[2];
            String strEmail = email[0];
            String emailDomain = email[1];
            model.addAttribute("user", user);
            model.addAttribute("middleNum", middleNum);
            model.addAttribute("lastNum", lastNum);
            model.addAttribute("strEmail", strEmail);
            model.addAttribute("emailDomain", emailDomain);
        }

        String[] cartIndex = cartList.split(",");

        List<CartDto> cartDtoList = productService.cartOrderList(cartIndex);

        model.addAttribute("cartDtoList", cartDtoList);

        return "user/product/order";
    }

    @GetMapping("/user/detail/order")
    public String userDetailOrder(@RequestParam("productId") String productId,
                                  @RequestParam("option") String option,
                                  @RequestParam("amount") String amount,
                                  @RequestParam("price") String price,
                                  Principal principal, Model model){

        if(principal != null){
            UserDto user = userService.detail(principal.getName());
            String[] phone = user.getPhone().split("-");
            String[] email = user.getUserId().split("@");
            String middleNum = phone[1];
            String lastNum = phone[2];
            String strEmail = email[0];
            String emailDomain = email[1];
            model.addAttribute("user", user);
            model.addAttribute("middleNum", middleNum);
            model.addAttribute("lastNum", lastNum);
            model.addAttribute("strEmail", strEmail);
            model.addAttribute("emailDomain", emailDomain);
        }

        Optional<Product> optionalProduct = productRepository.findById(parseLong(productId));

        if (optionalProduct.isEmpty()) {
            return null;
        }
        Product product = optionalProduct.get();
        model.addAttribute("product", product);
        model.addAttribute("option", option);
        model.addAttribute("amount", amount);
        model.addAttribute("price", price);
        model.addAttribute("regDt", LocalDate.now());

        return "user/product/order";
    }

    @PostMapping ("/user/order/result")
    public String userOrderResult(UserOrderInput parameter, Principal principal, Model model){

        String userId = principal.getName();
        parameter.setUserId(userId);
        String orderName = parameter.getOrderName2();
        String message = parameter.getSendMessage();
        boolean result = userOrderService.orderResult(parameter);

        String productPriceList = parameter.getProductPriceList();
        String[] productPrice = productPriceList.split(",");

        String productAmountList = parameter.getProductAmountList();
        String[] productAmount = productAmountList.split(",");

        String productUrlFileList = parameter.getProductUrlFileList();
        String[] productUrlFile = productUrlFileList.split(",");

        String productNameList = parameter.getProductNameList();
        String[] productName = productNameList.split(",");

        String productOptionList = parameter.getProductOptionList();
        String[] productOption = productOptionList.split(",");

        List<UserOrderProductInput> orderList = new ArrayList<>();

        for (int i = 0; i < productName.length; i++) {
            UserOrderProductInput orderProduct = UserOrderProductInput.builder()
                    .redDt(LocalDate.now())
                    .urlFilename(productUrlFile[i])
                    .productName(productName[i])
                    .amount(parseLong(productAmount[i]))
                    .price(parseLong(productPrice[i]))
                    .productOption(productOption[i])
                    .build();
            orderList.add(orderProduct);
        }

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderName", orderName);
        model.addAttribute("message", message);

        return "user/product/orderResult";
    }

    @GetMapping("/user/order/orderList")
    public String userOrderList(@RequestParam(value = "date", required = false) String date, Principal principal, Model model){

        String userId = principal.getName();
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "regDt");
        if (date != null) {
            if(date.equals("1")){
                LocalDateTime startDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
                LocalDateTime endDate = startDate.plusDays(1);
                List<UserOrder> orderList = userOrderRepository.findAllByRegDtBetween(startDate, endDate, sort);
                model.addAttribute("orderList", orderList);
            }
            if(date.equals("2")){
                LocalDateTime endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59 , 59);
                LocalDateTime startDate = endDate.minusDays(1).minusWeeks(1);
                List<UserOrder> orderList = userOrderRepository.findAllByRegDtBetween(startDate, endDate, sort);
                model.addAttribute("orderList", orderList);
            }
            if(date.equals("3")){
                LocalDateTime endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59 , 59);
                LocalDateTime startDate = endDate.minusDays(1).minusMonths(1);
                List<UserOrder> orderList = userOrderRepository.findAllByRegDtBetween(startDate, endDate, sort);
                model.addAttribute("orderList", orderList);
            }
            if(date.equals("4")){
                LocalDateTime endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59 , 59);
                LocalDateTime startDate = endDate.minusDays(1).minusMonths(3);
                List<UserOrder> orderList = userOrderRepository.findAllByRegDtBetween(startDate, endDate, sort);
                model.addAttribute("orderList", orderList);
            }
        }else{
            List<UserOrderDto> orderList = userOrderService.orderList(userId);
            model.addAttribute("orderList", orderList);
        }

        return "user/product/orderList";
    }

    @GetMapping("/user/product/review")
    public String productReview(Model model, @RequestParam("id") long id, @RequestParam("option") String option){

        ProductDto product = productService.detail(id);

        model.addAttribute("product", product);
        model.addAttribute("option", option);

        return "user/product/review";
    }

    @PostMapping("/user/product/review")
    public String productReviewAddSubmit(Model model, ReviewInput parameter
            , MultipartFile file, Principal principal){

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
        if(principal.getName() != null){
            parameter.setUserId(principal.getName());
        }

        parameter.setFilename(saveFilename);
        parameter.setUrlFilename(urlFilename);

        System.out.println("============================================");
        System.out.println("Parameter : " + parameter.toString());
        boolean result = reviewService.add(parameter);

        return "redirect:/user/product/detail?id=" + parameter.getProductId();
    }

    @GetMapping("/user/update/review")
    public String productReviewUpdate(Model model, @RequestParam("id") long id, @RequestParam("option") String option,
                                      @RequestParam("reviewId") long reviewId){

        ProductDto product = productService.detail(id);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            return null;
        }

        Review review = optionalReview.get();

        model.addAttribute("product", product);
        model.addAttribute("option", option);
        model.addAttribute("review", review);

        return "user/product/reviewUpdate";
    }

    @PostMapping("/user/update/review")
    public String productReviewUpdateSubmit(Model model, ReviewInput parameter
            , MultipartFile file, Principal principal){

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
        if(principal.getName() != null){
            parameter.setUserId(principal.getName());
        }

        parameter.setFilename(saveFilename);
        parameter.setUrlFilename(urlFilename);


        boolean result = reviewService.update(parameter);

        return "redirect:/user/product/detail?id=" + parameter.getProductId();
    }

    @GetMapping("/user/delete/review")
    public String productReviewDelete(Model model, @RequestParam("id") long id, @RequestParam("productId") long productId){

        boolean result = reviewService.delete(id);

        return "redirect:/user/product/detail?id=" + productId;
    }

    @GetMapping("/user/product/inquiry")
    public String productInquiry(Model model, Principal principal){

        if (principal == null) {
            return null;
        }
        String userid = principal.getName();
        Sort sort = Sort.by(Sort.Direction.DESC, "regDt");
        List<Inquiry> inquiryList = inquiryRepository.findAllByUserId(userid, sort);


        model.addAttribute("inquiryList", inquiryList);

        return "user/product/inquiry";
    }

    @GetMapping("/user/product/addInquiry")
    public String productInquiry(Principal principal, Model model){

        if(principal == null){
            return null;
        }

        String userId = principal.getName();

        Sort sort = Sort.by(Sort.Direction.DESC, "regDt");

        Optional<User> optionalUser = userRepository.findById(userId);
        List<UserOrder> orderList = userOrderRepository.findAllByUserId(userId, sort);

        if(optionalUser.isEmpty()){
            return null;
        }

        User user = optionalUser.get();


        model.addAttribute("user", user);
        model.addAttribute("orderList", orderList);

        return "user/product/addInquiry";
    }

    @PostMapping("/user/product/addInquiry")
    public String productAddInquiry(InquiryInput parameter, MultipartFile file){

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

        boolean result = userInquiryService.add(parameter);

        return "redirect:/user/product/inquiry";
    }

    @GetMapping("/user/inquiry/check")
    public String inquiryCheck(@RequestParam("id") long id, Model model){

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

        return "user/product/inquiryCheck";
    }

}
