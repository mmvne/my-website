package com.example.mywebsite.user.service;

import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.user.dto.UserOrderDto;
import com.example.mywebsite.user.entity.UserOrder;
import com.example.mywebsite.user.model.UserOrderInput;
import com.example.mywebsite.user.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.mywebsite.user.entity.OrderCode.ORDER_STATUS_CHECK;
import static java.lang.Long.parseLong;

@RequiredArgsConstructor
@Service
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderRepository userOrderRepository;
    private final ProductRepository productRepository;

    @Override
    public boolean orderResult(UserOrderInput parameter) {

        String productIndexList = parameter.getProductIndexList();
        String[] productIndex = productIndexList.split(",");

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

        String phone1 = parameter.getFirstCellPhoneNum2();
        String phone2 = parameter.getMiddleCellPhoneNum2();
        String phone3 = parameter.getLastCellPhoneNum2();

        String phone = phone1 + "-" + phone2 + "-" + phone3;

        System.out.println(Arrays.toString(productOption));

        for (int i = 0; i < productIndex.length; i++) {

            Optional<Product> optionalProduct = productRepository.findById(parseLong(productIndex[i]));

            if (optionalProduct.isEmpty()) {
                return false;
            }

            Product product = optionalProduct.get();
            product.setSaleCount(product.getSaleCount() + parseLong(productAmount[i]));

            if (productOption[i].equals("Free")) {
                if (product.getSizeFree() - parseLong(productAmount[i]) < 0 || product.getSizeFree() == 0) {
                    return false;
                }
                product.setSizeFree(product.getSizeFree() - parseLong(productAmount[i]));
            }
            if (productOption[i].equals("L")) {
                if (product.getSizeLarge() - parseLong(productAmount[i]) < 0 || product.getSizeLarge() == 0) {
                    return false;
                }
                product.setSizeLarge(product.getSizeLarge() - parseLong(productAmount[i]));
            }
            if (productOption[i].equals("XL")) {
                if (product.getSizeXlarge() - parseLong(productAmount[i]) < 0 || product.getSizeXlarge() == 0) {
                    return false;
                }
                product.setSizeXlarge(product.getSizeXlarge() - parseLong(productAmount[i]));
            }
            productRepository.save(product);

            UserOrder userOrder = UserOrder.builder()
                    .userId(parameter.getUserId())
                    .productId(parseLong(productIndex[i]))
                    .regDt(LocalDateTime.now())
                    .address1(parameter.getPostNo2())
                    .address2(parameter.getAddress3())
                    .address3(parameter.getAddress4())
                    .name(parameter.getOrderName2())
                    .phone(phone)
                    .productName(productName[i])
                    .productOption(productOption[i])
                    .urlFilename(productUrlFile[i])
                    .amount(parseLong(productAmount[i]))
                    .price(parseLong(productPrice[i]))
                    .orderRequest(parameter.getSendMessage())
                    .orderName(parameter.getSendName())
                    .orderStatus(ORDER_STATUS_CHECK)
                    .build();
            userOrderRepository.save(userOrder);
        }

        return true;
    }

    @Override
    public List<UserOrderDto> orderList(String userId) {

        Sort sort = Sort.by(Sort.Direction.DESC, "regDt");

        List<UserOrder> orderList = userOrderRepository.findAllByUserId(userId, sort);

        return UserOrderDto.of(orderList);
    }


}
