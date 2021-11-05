package com.resitplatform.infrastructure.db.jpa;

import com.resitplatform.domain.model.Resit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataResitRepository extends CrudRepository<Resit, UUID> {

    Optional<Resit> findBySlug(String slug);

    Optional<Resit> findByName(String name);

    // todo
    @Query("SELECT DISTINCT flower_ FROM Resit flower_ " +
            "WHERE " +
            "flower_.price = :price")
    List<Resit> findByFilters(@Param("price") Double price,
                                Pageable pageable);

    // todo
    @Query("SELECT flower_ FROM Resit flower_ JOIN flower_.owners owner WHERE owner.id IN :owners")
    List<Resit> findByOwners(List<UUID> owners, Pageable pageable);

}
