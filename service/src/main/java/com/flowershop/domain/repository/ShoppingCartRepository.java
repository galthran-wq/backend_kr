package com.flowershop.domain.repository;

import com.flowershop.domain.model.ShoppingCart;

public interface ShoppingCartRepository {
    ShoppingCart save(ShoppingCart shoppingCart);
}
