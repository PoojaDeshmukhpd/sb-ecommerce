package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
