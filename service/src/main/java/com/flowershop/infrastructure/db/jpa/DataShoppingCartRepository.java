package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.ShoppingCart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DataShoppingCartRepository extends CrudRepository<ShoppingCart, UUID> {
    @Override
    <S extends ShoppingCart> S save(S entity);
}
