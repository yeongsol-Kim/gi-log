package com.gilog.repository;

import com.gilog.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryInt extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
