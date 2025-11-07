package com.quickbasket.quickbasket.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String shop;
    private String address;
    private String longitude;
    private String latitude;
    private String contactNumber;
}
