package com.quickbasket.quickbasket.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ShopRepository extends JpaRepository<Shop, String> {
}
