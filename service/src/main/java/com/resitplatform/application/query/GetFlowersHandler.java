package com.resitplatform.application.query;

import com.resitplatform.api.dto.FlowerDto;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.api.query.GetResitsResult;
import com.resitplatform.application.FlowerAssembler;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.Flower;
import com.resitplatform.domain.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetFlowersHandler implements QueryHandler<GetResitsResult, GetResits> {

    private final FlowerRepository flowerRepository;

    @Override
    public GetResitsResult handle(GetResits query) {
        List<Flower> flowerList = flowerRepository
                .findByFilters(query.getPrice(), query.getLimit(), query.getOffset());

        List<FlowerDto> results = new ArrayList<>();

        flowerList.forEach(flower -> results.add(FlowerAssembler.assemble(flower)));

        return new GetResitsResult(results, results.size());
    }
}
