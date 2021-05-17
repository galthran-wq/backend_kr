package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.ShoppingCart;
import com.flowershop.domain.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaShoppingCartRepositoryAdapter implements ShoppingCartRepository {

    private final DataShoppingCartRepository repository;

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return repository.save(shoppingCart);
    }
}
