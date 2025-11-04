package com.quickbasket.quickbasket.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateShopRequest {

    private String email;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String state;
    private String slug;
    private String logo;
}
