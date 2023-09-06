package com.kaziamyr.onlinebookstore.repository;

import com.kaziamyr.onlinebookstore.model.CartItem;
import com.kaziamyr.onlinebookstore.model.ShoppingCart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByShoppingCart(ShoppingCart shoppingCart);
}
