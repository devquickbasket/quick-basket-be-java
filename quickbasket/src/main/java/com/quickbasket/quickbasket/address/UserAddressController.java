package com.quickbasket.quickbasket.address;

import com.quickbasket.quickbasket.customs.response.ApiResponse;
import com.quickbasket.quickbasket.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users/address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;


    @GetMapping("/index")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getUserAddresses() {
        try{

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

            List<UserAddressResponse> userAddresses = userAddressService.userAddress(userDetails.getId());

            ApiResponse response = new ApiResponse();

            response.setMessage("Success");
            response.setData(userAddresses);
            response.setSuccess(Boolean.TRUE);

            return ResponseEntity.ok(response);

        }catch(Exception e){
            ApiResponse apiResponse = new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            );

            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/store")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> storeUserAddress(@RequestBody AddUserAddressRequest userAddressRequest) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String userId = userDetails.getId();

            UserAddressResponse userAddress =  userAddressService.storeAddress(userId,userAddressRequest);

            ApiResponse response = new ApiResponse(true,"success",userAddress);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PatchMapping("/change-status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> changeStatus(@RequestBody ChangeStatusRequest request){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String userId = userDetails.getId();

            UserAddressResponse userAddressResponse = userAddressService.changeStatus(userId,request.getAddressId());

            return ResponseEntity.ok(new ApiResponse(true,"status change successfully",userAddressResponse));

        }catch(Exception e){
            ApiResponse apiResponse = new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
}
