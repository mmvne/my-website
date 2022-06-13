package com.example.mywebsite.admin.repository;

import com.example.mywebsite.admin.entity.ProductLike;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;


public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    Optional<ProductLike> findByUserIdAndProductId(String userId, Long productId);

    int countByProductId(long productId);

    @Transactional
    @Modifying
    @Query("delete from ProductLike pl where pl.userId = ?1 and pl.productId = ?2")
    void deleteByUserIdAndProductId(@Param("user_id") String userId, @Param("product_id") long productId);
}
