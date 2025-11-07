package com.quickbasket.quickbasket.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, String> {
    List<Shop> findByStatus(Integer status);

}
