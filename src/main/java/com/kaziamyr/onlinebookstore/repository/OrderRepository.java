package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.Order;
import com.kaziamyr.onlinebookstore.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
