package com.quickbasket.quickbasket.user;

import com.quickbasket.quickbasket.Otp.OtpService;
import com.quickbasket.quickbasket.Otp.OtpVerificationRequest;
import com.quickbasket.quickbasket.customs.response.ApiResponse;
import com.quickbasket.quickbasket.customs.response.LoginResponse;
import com.quickbasket.quickbasket.user.requests.LoginRequest;
import com.quickbasket.quickbasket.user.requests.UserFilterRequest;
import com.quickbasket.quickbasket.user.requests.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;  // ✅ Now properly injected
    private final OtpService otpService;    // ✅ Now properly injected

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        try {
            request.setRole("ROLE_USER");
            User user = userService.register(request);
            otpService.generateAndSendOtp(user.getEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, "User registration successful", user));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, exception.getMessage(), null));
        }
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        try {
            boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());

            if (!valid) throw new BadRequestException("Invalid OTP");

            User user = userService.activateUser(request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "User verification successful", user));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse login = userService.login(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "User login successful", login));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, exception.getMessage(), null));
        }
    }

    @GetMapping("/admin/index")
    @PreAuthorize("hasRole('ADMIN')")  // ✅ Works with ROLE_ADMIN
    public ResponseEntity<ApiResponse> allUsers(@ModelAttribute UserFilterRequest request) {
        try {
            Page<UserResponse> users = userService.getAllUsers(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Users found", users));
        } catch (Exception ex) {
            log.error("all users error", ex);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", user));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
