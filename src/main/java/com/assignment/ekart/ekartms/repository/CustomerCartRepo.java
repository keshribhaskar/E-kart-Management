package com.assignment.ekart.ekartms.repository;

import com.assignment.ekart.ekartms.entity.CustomerCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerCartRepo extends JpaRepository<CustomerCartEntity,Integer> {
    Optional<CustomerCartEntity> findByCustomerEmailId(String customerEmailId);
}
