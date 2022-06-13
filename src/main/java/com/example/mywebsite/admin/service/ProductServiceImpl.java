package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.dto.ProductDto;
import com.example.mywebsite.user.dto.CartDto;
import com.example.mywebsite.user.entity.Cart;
import com.example.mywebsite.admin.entity.Product;
import com.example.mywebsite.admin.entity.ProductLike;
import com.example.mywebsite.admin.model.ProductInput;
import com.example.mywebsite.admin.model.ProductLikeInput;
import com.example.mywebsite.admin.repository.ProductLikeRepository;
import com.example.mywebsite.admin.repository.ProductRepository;
import com.example.mywebsite.user.model.UserCartInput;
import com.example.mywebsite.user.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final CartRepository cartRepository;

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
                .saleCount(0L)
                .reviewScore(0L)
                .reviewCount(0L)
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

    @Override
    public ProductDto detail(long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = optionalProduct.get();

        return ProductDto.of(product);
    }

    @Override
    public boolean addLike(ProductLikeInput parameter) {

        Optional<ProductLike> optionalProductLike =
                productLikeRepository.findByUserIdAndProductId(parameter.getUserId(), parameter.getProductId());

        if(optionalProductLike.isPresent()){

            productLikeRepository.deleteByUserIdAndProductId(parameter.getUserId(), parameter.getProductId());

            return true;
        }

        ProductLike productLike = ProductLike.builder()
                .userId(parameter.getUserId())
                .productId(parameter.getProductId())
                .regDt(LocalDateTime.now())
                .build();

        productLikeRepository.save(productLike);

        return true;
    }

    @Override
    public boolean addCart(UserCartInput parameter) {

        Cart cart = Cart.builder()
                .productId(parameter.getProductId())
                .userId(parameter.getUserId())
                .productName(parameter.getProductName())
                .urlFilename(parameter.getUrlFilename())
                .option(parameter.getOption())
                .amount(parameter.getAmount())
                .price(parameter.getPrice())
                .regDt(LocalDate.now())
                .build();

        cartRepository.save(cart);

        return true;
    }

    @Override
    public List<CartDto> cartList(String userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDt");
        List<Cart> carts = cartRepository.findAllCartByUserId(userId, sort);
        return CartDto.of(carts);
    }

    @Override
    public boolean delCart(UserCartInput parameter) {
        cartRepository.deleteById(parameter.getCartId());
        return true;
    }

    @Override
    public boolean cartUpdate(long id, long amount, int price, String userId) {

        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()) {
            return false;
        }
        Cart cart = optionalCart.get();

        if(!cart.getUserId().equals(userId)){
            return false;
        }

        cart.setAmount(amount);
        cart.setPrice(price);
        cartRepository.save(cart);

        return true;
    }

    @Override
    public List<CartDto> cartOrderList(String[] cartIndex) {
        List<CartDto> cartDtoList = new ArrayList<>();
        for(String cartId : cartIndex){
            Optional<Cart> optionalCart = cartRepository.findById(Long.parseLong(cartId));
            if (optionalCart.isEmpty()) {
                return null;
            }
            Cart cart = optionalCart.get();
            cartDtoList.add(CartDto.of(cart));
        }

        return cartDtoList;
    }

    @Override
    public List<ProductDto> relatedList(Long categoryId) {

        List<Product> productList = productRepository.findAllByCategoryId(categoryId);

        if (productList.isEmpty()) {
            return null;
        }

        return ProductDto.of(productList);
    }

}
