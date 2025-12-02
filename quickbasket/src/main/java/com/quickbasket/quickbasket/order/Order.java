package com.quickbasket.quickbasket.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quickbasket.quickbasket.address.UserAddress;
import com.quickbasket.quickbasket.customs.Utils.BaseEntity;
import com.quickbasket.quickbasket.orderItem.OrderItem;
import com.quickbasket.quickbasket.shop.Shop;
import com.quickbasket.quickbasket.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();;

    private String orderNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal distance;

    private String address;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedTotal = new BigDecimal("0");

    @Column(precision = 10, scale = 2)
    private BigDecimal actualTotal = new BigDecimal("0");

    private String contactNumber;

    @Column(precision = 10, scale = 2)
    private String longitude;
    @Column(precision = 10, scale = 2)
    private String latitude;

    private Integer status = 100;
}
