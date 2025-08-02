package com.qeat.domain.store.service;

import com.qeat.domain.store.dto.response.StoreBookmarkListResponse;
import com.qeat.domain.store.entity.Store;
import com.qeat.domain.store.entity.StoreBookmark;
import com.qeat.domain.store.exception.code.StoreErrorCode;
import com.qeat.domain.store.repository.StoreBookmarkRepository;
import com.qeat.domain.store.repository.StoreRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import com.qeat.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreBookmarkService {

    private final StoreBookmarkRepository storeBookmarkRepository;
    private final StoreRepository storeRepository;

    public void bookmarkStore(Long userId, Long storeId) {
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

    public void unbookmarkStore(Long userId, Long storeId) {
        StoreBookmark bookmark = storeBookmarkRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_BOOKMARK_NOT_FOUND));

        storeBookmarkRepository.delete(bookmark);
    }

    public List<StoreBookmarkListResponse> getBookmarkedStores(Long userId) {
        List<StoreBookmark> bookmarks = storeBookmarkRepository.findAllByUserId(userId);

        return bookmarks.stream().map(bookmark -> {
            Store store = storeRepository.findById(bookmark.getStoreId())
                    .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));
            return StoreBookmarkListResponse.builder()
                    .storeId(store.getId())
                    .name(store.getName())
                    .location(store.getLocation())
                    .category(
                            StoreBookmarkListResponse.CategoryResponse.builder()
                                    .id(store.getCategory().getId())
                                    .name(store.getCategory().getName())
                                    .build()
                    )
                    .isBookmarked(true)
                    .build();
        }).toList();
    }
}