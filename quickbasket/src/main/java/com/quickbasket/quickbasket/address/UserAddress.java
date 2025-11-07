package com.quickbasket.quickbasket.address;

import com.quickbasket.quickbasket.customs.Utils.BaseEntity;
import com.quickbasket.quickbasket.order.Order;
import com.quickbasket.quickbasket.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = true)
    private String street;
    private String city;
    private String state;
    private String longitude;
    private String latitude;
    private Integer status = 100;
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
