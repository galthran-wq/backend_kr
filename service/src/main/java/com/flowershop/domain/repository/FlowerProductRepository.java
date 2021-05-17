package com.flowershop.domain.repository;

import com.flowershop.domain.model.FlowerProduct;

public interface FlowerProductRepository {
    FlowerProduct save(FlowerProduct entity);
}
