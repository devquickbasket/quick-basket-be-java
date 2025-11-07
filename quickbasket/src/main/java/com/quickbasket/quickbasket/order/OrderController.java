package com.quickbasket.quickbasket.order;

import com.quickbasket.quickbasket.customs.response.ApiResponse;
import com.quickbasket.quickbasket.order.request.CreateOrderRequest;
import com.quickbasket.quickbasket.orderItem.AddOrderItemRequest;
import com.quickbasket.quickbasket.orderItem.OrderItem;
import com.quickbasket.quickbasket.orderItem.OrderItemResponse;
import com.quickbasket.quickbasket.orderItem.OrderItemService;
import com.quickbasket.quickbasket.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("/index")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> userOrders() {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String userId = userDetails.getId();

            Page<OrderResponse> orderResponseList = orderService.userOrders(userId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Orders found", orderResponseList));
        }catch(Exception ex){
            log.error(ex.getMessage(),ex);
            return ResponseEntity.badRequest().body(new ApiResponse(false,ex.getMessage(),ex));
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createUserOrder(@RequestBody CreateOrderRequest request) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            String userId = userDetails.getId();

            OrderResponse orderResponseList = orderService.createOrder(userId,request);

            return ResponseEntity.ok(new ApiResponse<>(true, "Orders found", orderResponseList));
        }catch(Exception ex){
            log.error(ex.getMessage(),ex);
            return ResponseEntity.badRequest().body(new ApiResponse(false,ex.getMessage(),ex));
        }
    }

    @PostMapping("/{order}/addItems")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createUserOrder(@PathVariable String order,
                                                       @RequestBody AddOrderItemRequest request) {
        try{
          OrderItemResponse orderResponseList = orderService.addItem(order,request);

            return ResponseEntity.ok(new ApiResponse<>(true, "Orders found", orderResponseList));
        }catch(Exception ex){
            log.error(ex.getMessage(),ex);
            return ResponseEntity.badRequest().body(new ApiResponse(false,ex.getMessage(),ex));
        }
    }






}
