package com.qeat.domain.store.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record StoreDetailResponse(
        Long storeId,
        String name,
        CategoryDto category,
        String location,
        String locationDescription,
        String phoneNumber,
        String operatingHour,
        String mainMenu,
        String website,
        String instagram,
        String storeDescription,
        List<MenuDto> menu
) {
    @Builder
    public record CategoryDto(Long id, String name) {}

    @Builder
    public record MenuDto(Long menuId, String name, int price, String menuDescription) {}
}