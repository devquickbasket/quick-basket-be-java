package com.quickbasket.quickbasket.orderItem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderItemRequest {
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private String description;
    private BigDecimal totalPrice;
    private BigDecimal actualPrice;
    private Integer status;
    private String itemImage;
}
