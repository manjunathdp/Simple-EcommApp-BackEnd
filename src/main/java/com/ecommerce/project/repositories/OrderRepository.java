package com.ecommerce.project.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}