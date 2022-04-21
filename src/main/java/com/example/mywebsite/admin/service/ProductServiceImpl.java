package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private LocalDate getLocalDate(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public boolean add(ProductInput parameter) {

        System.out.println(" PARM : " + parameter.toString());

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Product product = Product.builder()
                .categoryId(parameter.getCategoryId())
                .keyword(parameter.getKeyword())
                .productName(parameter.getProductName())
                .regDt(LocalDateTime.now())
                .gender(parameter.getGender())
                .contents(parameter.getContents())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                .saleYn(parameter.isSaleYn())
                .saleEndDt(saleEndDt)
                .sizeLarge(parameter.getSizeLarge())
                .sizeXlarge(parameter.getSizeXlarge())
                .sizeFree(parameter.getSizeFree())
                .likeCount(0L)
                .saleCount(0L)
                .filename(parameter.getFilename())
                .urlFilename(parameter.getUrlFilename())
                .build();
        productRepository.save(product);

        return true;
    }

    @Override
    public boolean delete(long id) {

        productRepository.deleteById(id);

        return true;
    }

    @Override
    public ProductDto update(long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        Product product = optionalProduct.get();


        return ProductDto.of(product);
    }

    @Override
    public boolean set(ProductInput parameter) {

        LocalDate saleEndDt = getLocalDate(parameter.getSaleEndDtText());

        Optional<Product> optionalProduct = productRepository.findById(parameter.getProductId());

        if(optionalProduct.isEmpty()){
            return false;
        }

        Product product = optionalProduct.get();
        product.setCategoryId(parameter.getCategoryId());
        product.setKeyword(parameter.getKeyword());
        product.setProductName(parameter.getProductName());
        product.setGender(parameter.getGender());
        product.setUdtDt(LocalDateTime.now());
        product.setContents(parameter.getContents());
        product.setPrice(parameter.getPrice());
        product.setSalePrice(parameter.getSalePrice());
        product.setSaleYn(parameter.isSaleYn());
        product.setSaleEndDt(saleEndDt);
        product.setSizeLarge(parameter.getSizeLarge());
        product.setSizeXlarge(parameter.getSizeXlarge());
        product.setSizeFree(parameter.getSizeFree());
        product.setFilename(parameter.getFilename());
        product.setUrlFilename(parameter.getUrlFilename());
        productRepository.save(product);

        return true;
    }

}
