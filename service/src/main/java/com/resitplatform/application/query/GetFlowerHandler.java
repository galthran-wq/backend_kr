package com.resitplatform.application.query;

import com.resitplatform.api.query.GetResit;
import com.resitplatform.api.query.GetResitResult;
import com.resitplatform.application.FlowerAssembler;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.Flower;
import com.resitplatform.domain.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetFlowerHandler implements QueryHandler<GetResitResult, GetResit> {

    private final FlowerRepository flowerRepository;

    @Override
    public GetResitResult handle(GetResit query) {
        Flower flowerBySlug = flowerRepository.findBySlug(query.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No flower with slug \"[slug=%s]\" found", query.getSlug()));

        return new GetResitResult(FlowerAssembler.assemble(flowerBySlug));
    }
}
