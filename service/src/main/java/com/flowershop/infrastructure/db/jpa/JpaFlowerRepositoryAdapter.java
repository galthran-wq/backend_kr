package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.Flower;
import com.flowershop.domain.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class JpaFlowerRepositoryAdapter implements FlowerRepository {

    private final DataFlowerRepository repository;

    @Override
    public Optional<Flower> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    @Override
    public Optional<Flower> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Flower> findByPrice(Double price) {
        return repository.findByPrice(price);
    }

    @Override
    public List<Flower> findByOwners(List<UUID> owners, Integer limit, Integer offset) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Order.desc("name")));
        return repository.findByOwners(owners, pageable);
    }

    @Override
    public List<Flower> findByFilters(Double price, Integer limit, Integer offset) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Order.desc("name")));
        return repository.findByFilters(price, pageable);
    }

    @Override
    public void delete(Flower flower) {
        repository.delete(flower);
    }

    @Override
    public Flower save(Flower flower) {
        return repository.save(flower);
    }

}
