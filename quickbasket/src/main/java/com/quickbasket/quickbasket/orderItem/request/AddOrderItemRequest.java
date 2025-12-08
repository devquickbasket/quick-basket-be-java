package com.quickbasket.quickbasket.orderItem.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderItemRequest {
    @NotNull(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Item quantity is required")
    @Min(1)
    private Integer quantity;

    private BigDecimal price;
    private String description;
    private BigDecimal totalPrice;
    private BigDecimal actualPrice;
    private Integer status;
    private String itemImage;
}
