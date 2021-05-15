package com.flowershop.application.query;

import com.flowershop.api.dto.FlowerDto;
import com.flowershop.api.query.GetFlowers;
import com.flowershop.api.query.GetFlowersResult;
import com.flowershop.application.FlowerAssembler;
import com.flowershop.bus.QueryHandler;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetFlowersHandler implements QueryHandler<GetFlowersResult, GetFlowers> {

    private final FlowerRepository flowerRepository;

    @Override
    public GetFlowersResult handle(GetFlowers query) {
        List<Flower> flowerList = flowerRepository
                .findByFilters(query.getPrice(), query.getLimit(), query.getOffset());

        List<FlowerDto> results = new ArrayList<>();

        flowerList.forEach(flower -> results.add(FlowerAssembler.assemble(flower)));

        return new GetFlowersResult(results, results.size());
    }
}
