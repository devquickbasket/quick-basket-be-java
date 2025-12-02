package com.quickbasket.quickbasket.order;

import com.quickbasket.quickbasket.orderItem.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderWithItemsResponse extends OrderResponse {
    private OrderItemResponse orderItem;
}
