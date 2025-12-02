package com.quickbasket.quickbasket.orderItem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentUpdateItemRequest {
    private String orderItem;
    private Integer status;
    private BigDecimal actualPrice;
    private Integer availableQuantity;
}
