package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.FlowerProduct;
import com.flowershop.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DataFlowerProductRepository extends CrudRepository<FlowerProduct, UUID> {
    @Override
    <S extends FlowerProduct> S save(S entity);
}
