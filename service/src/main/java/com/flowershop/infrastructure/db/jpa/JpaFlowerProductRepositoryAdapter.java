package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.FlowerProduct;
import com.flowershop.domain.repository.FlowerProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaFlowerProductRepositoryAdapter implements FlowerProductRepository {
    private final DataFlowerProductRepository repository;

    @Override
    public FlowerProduct save(FlowerProduct entity) {
        return repository.save(entity);
    }
}
