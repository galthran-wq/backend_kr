package com.flowershop.application.query;

import com.flowershop.api.query.GetFlower;
import com.flowershop.api.query.GetFlowerResult;
import com.flowershop.application.FlowerAssembler;
import com.flowershop.application.exception.NotFoundException;
import com.flowershop.bus.QueryHandler;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetFlowerHandler implements QueryHandler<GetFlowerResult, GetFlower> {

    private final FlowerRepository flowerRepository;

    @Override
    public GetFlowerResult handle(GetFlower query) {
        Flower flowerBySlug = flowerRepository.findBySlug(query.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No flower with slug \"[slug=%s]\" found", query.getSlug()));

        return new GetFlowerResult(FlowerAssembler.assemble(flowerBySlug));
    }
}
