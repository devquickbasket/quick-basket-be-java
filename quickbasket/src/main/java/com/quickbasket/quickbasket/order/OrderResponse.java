package com.quickbasket.quickbasket.order;

import com.quickbasket.quickbasket.address.UserAddress;
import com.quickbasket.quickbasket.orderItem.OrderItemResponse;
import com.quickbasket.quickbasket.shop.Shop;
import com.quickbasket.quickbasket.shop.response.ShopResponse;
import com.quickbasket.quickbasket.user.User;
import com.quickbasket.quickbasket.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
    private UserResponse user;
    private ShopResponse shop;
    private String longitude;
    private String latitude;
    private String orderNumber;
    private BigDecimal distance;
    private BigDecimal deliveryFee;
    private BigDecimal expectedTotal;
    private BigDecimal actualTotal;
    private String address;
    private Integer status;
    private List<OrderItemResponse>  orderItems;

    public  OrderResponse(Order order) {
        this.id = order.getId();
        this.user = new UserResponse(order.getUser());
        this.orderNumber = order.getOrderNumber();
        this.distance = order.getDistance();
        this.deliveryFee = order.getDeliveryFee();
        this.expectedTotal = order.getExpectedTotal();
        this.actualTotal = order.getActualTotal();
        this.status = order.getStatus();
        this.shop = new  ShopResponse(order.getShop());
        this.latitude = order.getLatitude();
        this.longitude = order.getLongitude();
        this.address = order.getAddress();
        this.orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::new)
                .toList();

    }
}
