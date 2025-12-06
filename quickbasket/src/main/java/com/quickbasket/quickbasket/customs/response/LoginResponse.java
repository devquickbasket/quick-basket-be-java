package com.quickbasket.quickbasket.customs.response;

import com.quickbasket.quickbasket.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private Integer status;
    private String id;
    private String name;
    private String role;
}
