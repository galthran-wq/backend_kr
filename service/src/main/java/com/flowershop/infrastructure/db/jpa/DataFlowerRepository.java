package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.Flower;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataFlowerRepository extends CrudRepository<Flower, UUID> {

    Optional<Flower> findByPrice(Double price);

    Optional<Flower> findBySlug(String slug);

    Optional<Flower> findByName(String name);

    @Query("SELECT DISTINCT flower_ FROM Flower flower_ " +
            "WHERE " +
            "flower_.price = :price")
    List<Flower> findByFilters(@Param("price") Double price,
                                Pageable pageable);

    @Query("SELECT flower_ FROM Flower flower_ JOIN flower_.owners owner WHERE owner.id IN :owners")
    List<Flower> findByOwners(List<UUID> owners, Pageable pageable);

}
