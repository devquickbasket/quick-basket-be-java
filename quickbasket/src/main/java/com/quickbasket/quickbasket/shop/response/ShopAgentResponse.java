package com.quickbasket.quickbasket.shop.response;

import com.quickbasket.quickbasket.role.Role;
import com.quickbasket.quickbasket.user.User;
import lombok.Data;

import java.util.Set;

@Data
public class ShopAgentResponse {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private Integer status;

    public ShopAgentResponse(User user){
        this.id = user.getId();
        this.name = user.getFirstName()+" "+user.getLastName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.phoneNumber = user.getPhoneNumber();
    }
}
