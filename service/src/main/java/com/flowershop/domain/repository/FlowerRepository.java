package com.flowershop.domain.repository;

import com.flowershop.domain.model.Flower;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlowerRepository {

    Optional<Flower> findBySlug(String slug);

    Optional<Flower> findByName(String name);

    Optional<Flower> findByPrice(Double price);

    List<Flower> findByOwners(List<UUID> owners, Integer limit, Integer offset);

    List<Flower> findByFilters(Double price, Integer limit, Integer offset);

    void delete(Flower flower);

    Flower save(Flower flower);
}
