package com.qeat.domain.store.service;

import com.qeat.domain.store.dto.response.StoreDetailResponse;
import com.qeat.domain.store.entity.Store;
import com.qeat.domain.store.repository.StoreRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import com.qeat.domain.store.exception.code.StoreErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailResponse getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreErrorCode.STORE_NOT_FOUND));

        return StoreDetailResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .category(StoreDetailResponse.CategoryDto.builder()
                        .id(store.getCategory().getId())
                        .name(store.getCategory().getName())
                        .build())
                .location(store.getLocation())
                .locationDescription(store.getLocationDescription())
                .phoneNumber(store.getPhoneNumber())
                .operatingHour(store.getOperatingHour())
                .mainMenu(store.getMainMenu())
                .website(store.getWebsite())
                .instagram(store.getInstagram())
                .storeDescription(store.getStoreDescription())
                .menu(store.getMenuList().stream().map(menu -> StoreDetailResponse.MenuDto.builder()
                        .menuId(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .menuDescription(menu.getMenuDescription())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}