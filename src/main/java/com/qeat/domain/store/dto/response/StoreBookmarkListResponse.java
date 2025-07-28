package com.qeat.domain.store.dto.response;

import lombok.Builder;

@Builder
public record StoreBookmarkListResponse(
        Long storeId,
        String name,
        CategoryResponse category,
        String location,
        boolean isBookmarked
) {
    @Builder
    public record CategoryResponse(
            Long id,
            String name
    ) {}
}