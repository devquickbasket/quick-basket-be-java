package com.quickbasket.quickbasket.orderItem;

import com.quickbasket.quickbasket.customs.Utils.BaseEntity;
import com.quickbasket.quickbasket.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String itemName;
    private String itemDescription;

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal actualPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(precision = 10, scale = 2)
    private BigDecimal tax;

    private Integer quantity;
    private Integer status;
}
