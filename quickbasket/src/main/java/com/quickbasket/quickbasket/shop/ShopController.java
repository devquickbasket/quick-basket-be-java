package com.quickbasket.quickbasket.shop;

import com.quickbasket.quickbasket.customs.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/store")
    public ResponseEntity<ApiResponse> createShop(@RequestBody CreateShopRequest request) {
        try {
            ShopResponse shop = shopService.create(request);

            return ResponseEntity.ok(new ApiResponse(true, "success", shop));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @GetMapping("/index")
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
    @GetMapping("/{shopId}/show")
    public ResponseEntity<ApiResponse> show(@PathVariable String shopId) {
        try{
            ShopResponse shopResponse = shopService.show(shopId);

            return ResponseEntity.ok(new ApiResponse(true, "success", shopResponse));
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

}

