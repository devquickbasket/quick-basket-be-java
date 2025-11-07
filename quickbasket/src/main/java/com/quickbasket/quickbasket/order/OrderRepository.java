package com.quickbasket.quickbasket.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    Page<Order> findByUserId(String userId, Pageable pageable);
}
