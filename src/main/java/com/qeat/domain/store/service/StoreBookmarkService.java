package com.qeat.domain.store.service;

import com.qeat.domain.store.entity.StoreBookmark;
import com.qeat.domain.store.exception.code.StoreErrorCode;
import com.qeat.domain.store.repository.StoreBookmarkRepository;
import com.qeat.domain.store.repository.StoreRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreBookmarkService {

    private final StoreBookmarkRepository storeBookmarkRepository;
    private final StoreRepository storeRepository;

    public void bookmarkStore(Long storeId) {
        Long userId = extractUserId();

        if (!storeRepository.existsById(storeId)) {
            throw new CustomException(StoreErrorCode.STORE_NOT_FOUND);
        }

        if (storeBookmarkRepository.existsByUserIdAndStoreId(userId, storeId)) {
            return; // 이미 저장되어 있는 경우 무시
        }

        StoreBookmark bookmark = StoreBookmark.builder()
                .userId(userId)
                .storeId(storeId)
                .build();

        storeBookmarkRepository.save(bookmark);
    }

    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("사용자 인증 정보가 존재하지 않습니다.");
        }
        return (Long) authentication.getPrincipal();
    }

    public void unbookmarkStore(Long storeId) {
        Long userId = extractUserId();

        StoreBookmark bookmark = storeBookmarkRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_BOOKMARK_NOT_FOUND));

        storeBookmarkRepository.delete(bookmark);
    }
}