package com.quickbasket.quickbasket.orderItem;

import com.quickbasket.quickbasket.order.Order;
import com.quickbasket.quickbasket.order.OrderRepository;
import com.quickbasket.quickbasket.orderItem.request.AddOrderItemRequest;
import com.quickbasket.quickbasket.orderItem.request.AgentUpdateItemRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;

    public OrderItem createOrderItem(Order order, AddOrderItemRequest addOrderItemRequest) {
        OrderItem orderItem = new OrderItem(addOrderItemRequest);
        orderItem.setOrder(order);
        OrderItem savedItem = orderItemRepository.save(orderItem);
        return savedItem;
    }

    public OrderItemResponse updateOrderItem(AgentUpdateItemRequest request) {
        OrderItem orderItem = orderItemRepository.getById(request.getOrderItem());
        orderItem.setActualPrice(request.getActualPrice());
        orderItem.setQuantityAvailable(request.getAvailableQuantity());
        orderItem.setStatus(request.getStatus());
        Order order = orderItem.getOrder();

        order.setActualTotal(orderItem.getActualPrice().add(order.getActualTotal()));

        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        return new OrderItemResponse(orderItem);
    }
}
