package com.quickbasket.quickbasket.shop;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public Page<ShopResponse> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ShopResponse> shopPage = shopRepository.findAll(pageable).map(ShopResponse::new);

        return shopPage;
    }

    public ShopResponse create(CreateShopRequest request) {

        Shop shop = new Shop(request);

        shop.setStatus(200);

        shopRepository.save(shop);

        return new ShopResponse(shop);
    }

    public ShopResponse show(String shopId) {

        Shop shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {
            throw new RuntimeException("Shop with id " + shopId + " not found");
        }

        return new ShopResponse(shop);
    }
}
