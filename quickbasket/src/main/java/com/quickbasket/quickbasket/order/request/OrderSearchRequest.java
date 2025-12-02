package com.quickbasket.quickbasket.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderSearchRequest {
    private String shopId;
    private String userId;
    private String agentId;
    private Integer status;
    private Integer size = 20;
    private Integer page =0;
}
