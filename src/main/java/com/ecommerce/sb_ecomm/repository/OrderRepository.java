package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
