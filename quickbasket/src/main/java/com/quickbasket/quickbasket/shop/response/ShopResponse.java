package com.quickbasket.quickbasket.shop.response;

import com.quickbasket.quickbasket.shop.Shop;
import lombok.Data;

@Data
public class ShopResponse {

    private String id;
    private String name;
    private String address;
    private String email;
    private String longitude;
    private String latitude;
    private Integer status;
    private String phone;
    private String state;

    public ShopResponse(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.email = shop.getEmail();
        this.address = shop.getAddress();
        this.latitude = shop.getLatitude();
        this.longitude = shop.getLongitude();
        this.status = shop.getStatus();
        this.phone = shop.getPhoneNumber();
        this.state = shop.getState();
    }
}
