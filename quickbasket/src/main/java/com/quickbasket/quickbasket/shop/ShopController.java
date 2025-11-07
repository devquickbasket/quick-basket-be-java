package com.quickbasket.quickbasket.shop;

import com.quickbasket.quickbasket.customs.response.ApiResponse;
import com.quickbasket.quickbasket.shop.requests.AssignAgentRequest;
import com.quickbasket.quickbasket.shop.requests.CreateShopRequest;
import com.quickbasket.quickbasket.shop.response.ShopAgentResponse;
import com.quickbasket.quickbasket.shop.response.ShopResponse;
import com.quickbasket.quickbasket.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/store")
    public ResponseEntity<ApiResponse> createShop(@RequestBody CreateShopRequest request) {
        try {
            ShopResponse shop = shopService.create(request);

            return ResponseEntity.ok(new ApiResponse(true, "success", shop));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{shopId}/update")
    public ResponseEntity<ApiResponse> updateShop(@PathVariable String shopId,@RequestBody CreateShopRequest request) {
        try {
            ShopResponse shop = shopService.update(shopId,request);

            return ResponseEntity.ok(new ApiResponse(true, "success", shop));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @GetMapping("/admin/index")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> index(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ShopResponse> shopList = shopService.index(page, size);
            return ResponseEntity.ok(new ApiResponse(true, "success", shopList));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{shopId}/show")
    public ResponseEntity<ApiResponse> show(@PathVariable String shopId) {
        try{
            ShopResponse shopResponse = shopService.show(shopId);

            return ResponseEntity.ok(new ApiResponse(true, "success", shopResponse));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/assignAgent")
    public ResponseEntity<ApiResponse> AssignAgent(@RequestBody AssignAgentRequest request){
        try{

            ShopResponse shopResponse= shopService.assignAgent(request);

            return ResponseEntity.ok(new ApiResponse(true, "success", shopResponse));

        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @GetMapping("{shopId}/agents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> agents(@PathVariable String shopId) {
        try{
            List<ShopAgentResponse> agents = shopService.shopAgents(shopId);

            return ResponseEntity.ok(new ApiResponse(true, "success", agents));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> listShops() {
        try{
            List<ShopResponse> shops = shopService.shopAll();

            return ResponseEntity.ok(new ApiResponse(true, "success", shops));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

}

