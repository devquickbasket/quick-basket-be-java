package com.quickbasket.quickbasket.orderItem;

import com.quickbasket.quickbasket.order.Order;
import com.quickbasket.quickbasket.order.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String id;
    private String itemName;
    private Integer quantity;
    private BigDecimal expectedPrice;
    private String description;
    private BigDecimal totalPrice;
    private BigDecimal actualPrice;
    private Integer availableQuantity;
    private Integer status;
    private String itemImage;
    private OrderResponse orderResponse;

    public OrderItemResponse(OrderItem orderItem) {
        this.itemName = orderItem.getItemName();
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
//        this.orderResponse = new OrderResponse(orderItem.getOrder());
        this.availableQuantity = orderItem.getQuantityAvailable();
        this.status = orderItem.getStatus();
        this.totalPrice = orderItem.getExpectedTotalPrice();
        this.description = orderItem.getItemDescription();
        this.actualPrice = orderItem.getActualPrice();
        this.expectedPrice = orderItem.getExpectedPrice();
    }
}
