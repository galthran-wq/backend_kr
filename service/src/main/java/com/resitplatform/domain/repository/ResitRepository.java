package com.resitplatform.domain.repository;

import com.resitplatform.domain.model.Resit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResitRepository {

    Optional<Resit> findBySlug(String slug);

    Optional<Resit> findByName(String name);

    List<Resit> findByOwners(List<UUID> owners, Integer limit, Integer offset);

    List<Resit> findByFilters(Double price, Integer limit, Integer offset);

    void delete(Resit resit);

    Resit save(Resit resit);
}
