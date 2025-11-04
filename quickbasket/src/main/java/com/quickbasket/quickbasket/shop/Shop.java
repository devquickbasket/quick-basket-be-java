package com.quickbasket.quickbasket.shop;

import com.quickbasket.quickbasket.customs.Utils.BaseEntity;
import com.quickbasket.quickbasket.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String address;
    private String state;
    private String longitude;
    private String latitude;
    private String logo;
    private String email;
    private String slug;
    private Integer status = 200;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "shop_agents",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> agents = new ArrayList<>();

    public Shop(CreateShopRequest request) {
       address = request.getAddress();
       name = request.getName();
       slug = request.getSlug();
       email = request.getEmail();
       state = request.getState();
       longitude = request.getLongitude();
       latitude = request.getLatitude();
       logo = request.getLogo();
    }
}
