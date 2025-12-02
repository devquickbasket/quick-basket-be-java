package com.quickbasket.quickbasket.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "First name is required")
    private String lastName;

    @Email(message = "Email must be a valid mail")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password Required")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    @Null(message = "Phone number can be null")
    private String phoneNumber;

    private String role;
}
