package com.quickbasket.quickbasket.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {
    Page<Order> findByUserId(String userId, Pageable pageable);
    Page<Order> findByShopId(String shopId, Pageable pageable);
    Page<Order> findByShopIdAndStatus(String shopId, Integer status, Pageable pageable);
    Page<Order> findByShopIdAndUserId(String shopId, String userId, Pageable pageable);
    Page<Order> findByShopIdAndUserIdAndStatus(String shopId, String userId, Integer status,Pageable pageable);

    @Query("SELECT o FROM Order  o JOIN o.shop s JOIN s.agents a where a.id = :userId")
    public Page<Order> getAgentShopOrders(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT o FROM Order  o JOIN o.shop s JOIN s.agents a where a.id = :userId and o.status =:status")
    public Page<Order> getAgentShopOrdersByStatus(@Param("userId") String userId,
                                                  @Param("status") Integer status,Pageable pageable);

    public Page<Order> findByStatus(Integer status, Pageable pageable);

    @Query("SELECT o FROM Order  o JOIN o.shop s JOIN s.agents a where o.id = :orderId and a.id = :userId and o.status =:status")
    Optional<Order> getAgentShopOrder(String userId, int status, String orderId);
}
