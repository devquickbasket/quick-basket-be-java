package com.quickbasket.quickbasket.user;

import com.quickbasket.quickbasket.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Set<Role> roles;
    private Integer status;
    private String phoneNumber;

    public UserResponse(User user){
        this.id = user.getId();
        this.name = user.getFirstName()+" "+user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.status = user.getStatus();
        this.phoneNumber = user.getPhoneNumber();
    }
}
