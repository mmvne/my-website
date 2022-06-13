package com.example.mywebsite.admin.service;

import com.example.mywebsite.admin.model.OrderStatusInput;
import com.example.mywebsite.user.entity.UserOrder;
import com.example.mywebsite.user.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

   private final UserOrderRepository userOrderRepository;

   @Override
   public boolean updateStatus(OrderStatusInput parameter) {

      Optional<UserOrder> optionalUserOrder = userOrderRepository.findById(parameter.getOrderId());
      if (optionalUserOrder.isEmpty()) {
         return false;
      }

      UserOrder userOrder = optionalUserOrder.get();
      userOrder.setOrderStatus(parameter.getOrderStatus());
      userOrderRepository.save(userOrder);

      return true;
   }
}
