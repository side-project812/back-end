package com.qeat.domain.order.repository;

import com.qeat.domain.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    Optional<CartItem> findByUserIdAndStoreIdAndMenuId(Long userId, Long storeId, Long menuId);
}