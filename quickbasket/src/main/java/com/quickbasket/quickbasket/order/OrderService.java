package com.quickbasket.quickbasket.order;

import com.quickbasket.quickbasket.customs.Utils.DistanceCalculator;
import com.quickbasket.quickbasket.order.request.CreateOrderRequest;
import com.quickbasket.quickbasket.order.request.OrderSearchRequest;
import com.quickbasket.quickbasket.orderItem.request.AddOrderItemRequest;
import com.quickbasket.quickbasket.orderItem.OrderItem;
import com.quickbasket.quickbasket.orderItem.OrderItemResponse;
import com.quickbasket.quickbasket.orderItem.OrderItemService;
import com.quickbasket.quickbasket.shop.Shop;
import com.quickbasket.quickbasket.shop.ShopRepository;
import com.quickbasket.quickbasket.user.User;
import com.quickbasket.quickbasket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final OrderItemService orderItemService;
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

        Order orderEntity = orderRepository.findById(order).orElse(null);

        if (orderEntity == null) {
            throw new RuntimeException("Order not found");
        }

        OrderItem orderItem = orderItemService.createOrderItem(orderEntity,request);
        orderEntity.setExpectedTotal(orderEntity.getExpectedTotal().add(orderItem.getExpectedPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderRepository.save(orderEntity);

        return new OrderItemResponse(orderItem);
    }

    public Page<OrderResponse> agentOrders(String userId, OrderSearchRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        if (request.getStatus() != null) {
            Page<OrderResponse> orders = orderRepository.getAgentShopOrdersByStatus(userId,request.getStatus() ,pageable).map(order -> new OrderResponse(order));
            return orders;
        }
        Page<OrderResponse> orders = orderRepository.getAgentShopOrders(userId, pageable).map(order -> new OrderResponse(order));

        return orders;
    }

    public List<OrderItemResponse> orderItems(String orderId){
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        List<OrderItemResponse> orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::new)
                .toList();

        return orderItems;
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        return new OrderResponse(order);
    }

    public Page<OrderResponse> getAllOrders(OrderSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        if (request.getStatus() != null) {
            return orderRepository.findByStatus(request.getStatus(),pageable).map((order -> new OrderResponse(order)));
        }

        if (request.getShopId() != null) {
            return orderRepository.findByShopId(request.getShopId(),pageable).map((order -> new OrderResponse(order)));
        }

        return  orderRepository.findAll(pageable).map((order -> new OrderResponse(order)));
    }

    public OrderResponse acceptOrder(String userId, String orderId) {

      Optional<Order> order = orderRepository.getAgentShopOrder(userId,100 ,orderId);

      if (!order.isPresent()) {
          throw new RuntimeException("Order not found");
      }
      Order orderEntity = order.get();

      orderEntity.setStatus(200);
      orderEntity.setUpdatedAt(LocalDateTime.now());
      orderRepository.save(orderEntity);

      return new OrderResponse(orderEntity);
    }
}
