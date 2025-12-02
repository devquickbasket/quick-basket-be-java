package com.quickbasket.quickbasket.shop.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateShopRequest {

    private String email;
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Latitude is required for the shop")
    private String latitude;

    @NotBlank(message = "Longitude is required")
    private String longitude;

    @NotBlank(message = "Shop state is required")
    private String state;

    private String slug;
    private String phone;
    private String role;
    @NotBlank(message = "Phone number can be null")
    private String logo;
    private Integer status;
}
