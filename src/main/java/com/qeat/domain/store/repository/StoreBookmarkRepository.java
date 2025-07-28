package com.qeat.domain.store.repository;

import com.qeat.domain.store.entity.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
    Optional<StoreBookmark> findByUserIdAndStoreId(Long userId, Long storeId);
    List<StoreBookmark> findAllByUserId(Long userId);
}