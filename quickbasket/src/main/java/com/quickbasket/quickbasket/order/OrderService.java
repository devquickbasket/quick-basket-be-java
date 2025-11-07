package com.quickbasket.quickbasket.order;

import com.quickbasket.quickbasket.address.UserAddress;
import com.quickbasket.quickbasket.address.UserAddressRepository;
import com.quickbasket.quickbasket.customs.Utils.DistanceCalculator;
import com.quickbasket.quickbasket.order.request.CreateOrderRequest;
import com.quickbasket.quickbasket.orderItem.AddOrderItemRequest;
import com.quickbasket.quickbasket.orderItem.OrderItemResponse;
import com.quickbasket.quickbasket.shop.Shop;
import com.quickbasket.quickbasket.shop.ShopRepository;
import com.quickbasket.quickbasket.shop.ShopService;
import com.quickbasket.quickbasket.user.User;
import com.quickbasket.quickbasket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private static final SecureRandom random = new SecureRandom();

    public Page<OrderResponse> userOrders(String userId) {

        Page<OrderResponse> orders = orderRepository.findByUserId(userId, PageRequest.of(0, 20))
                .map(order -> new OrderResponse(order));


        return orders;
    }

    public OrderResponse createOrder(String userId, CreateOrderRequest createOrderRequest) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Shop shop = shopRepository.findById(createOrderRequest.getShop()).orElse(null);

        if (shop == null) {
            throw new RuntimeException("Shop not found");
        }

        double distanceKm = DistanceCalculator.calculateDistance(Double.parseDouble(shop.getLatitude()),
                Double.parseDouble(shop.getLongitude()),
                Double.parseDouble(createOrderRequest.getLatitude()),
                Double.parseDouble(createOrderRequest.getLongitude()));

        log.info("distance km is {}", distanceKm);

        BigDecimal deliveryFee = BigDecimal.valueOf(distanceKm* 500);

        log.info("deliveryFee is {}", deliveryFee);

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setShop(shop);
        order.setLongitude(createOrderRequest.getLongitude());
        order.setLatitude(createOrderRequest.getLatitude());
        order.setContactNumber(createOrderRequest.getContactNumber());
        order.setAddress(createOrderRequest.getAddress());
        order.setDeliveryFee(deliveryFee);
        order.setDistance(new BigDecimal(distanceKm));

        order.setStatus(100);

        Order orderSaved =  orderRepository.save(order);

        return new OrderResponse(orderSaved);
    }

    private static String generateOrderNumber() {
        String timestamp = DateTimeFormatter.ofPattern("yyMMddHHmm").format(LocalDateTime.now());
        int randomSuffix = 1000 + random.nextInt(9000); // 4-digit random
        return "ORD" + timestamp + randomSuffix;
    }

    public OrderItemResponse addItem(String order, AddOrderItemRequest request) {

        return new OrderItemResponse();
    }
}
