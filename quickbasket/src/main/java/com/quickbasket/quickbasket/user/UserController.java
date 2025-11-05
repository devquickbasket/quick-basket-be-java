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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal());
    }

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody UserRegistrationRequest request)
    {
        try{
            request.setRole("ROLE_USER");

            User user = userService.register(request);

            otpService.generateAndSendOtp(user.getEmail());

            ApiResponse<?> response = new ApiResponse<>(
                    true,
                    "User registration successful",
                    user
            );

           return ResponseEntity.ok(response);

        }catch (Exception exception){
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    false,
                    exception.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/otp/verify")
    private ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request)
    {
        try{
            boolean validateOtp = otpService.verifyOtp(request.getEmail(),request.getOtp());

            if(!validateOtp){
                throw  new BadRequestException("Invalid OTP");
            }
            User activateUser = userService.activateUser(request.getEmail());

            ApiResponse<?> response = new ApiResponse<>(
                    true,
                    "User verification successful",
                    activateUser
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            LoginResponse login = userService.login(request);

            ApiResponse<?> response = new ApiResponse<>(
                    true,
                    "User Login successful",
                    login
            );

            return ResponseEntity.ok(response);

        }catch (Exception exception){
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    false,
                    exception.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/admin/index")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> allUsers(@ModelAttribute UserFilterRequest request){
        try{
            Page<UserResponse> userResponsePage = userService.getAllUsers(request);

            return  ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Users found",
                    userResponsePage
            ));
        }
        catch (Exception ex){
            log.error("all users error", ex);
            ApiResponse<?> errorResponse = new ApiResponse<>();
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(UserRegistrationRequest request){

            try{
                User user = userService.register(request);
                ApiResponse<?> response = new ApiResponse<>(
                        true,
                        "User registration successful",
                        user
                );
                return ResponseEntity.ok(response);
            }catch (Exception ex){
                ApiResponse<?> errorResponse = new ApiResponse<>();
                errorResponse.setMessage(ex.getMessage());
                return ResponseEntity.badRequest().body(errorResponse);
            }

    }

}
