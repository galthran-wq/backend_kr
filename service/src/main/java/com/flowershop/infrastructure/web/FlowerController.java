package com.flowershop.infrastructure.web;

import com.flowershop.api.command.*;
import com.flowershop.api.operation.FlowerOperations;
import com.flowershop.api.query.GetFlower;
import com.flowershop.api.query.GetFlowerResult;
import com.flowershop.api.query.GetFlowers;
import com.flowershop.api.query.GetFlowersResult;
import com.flowershop.application.service.AuthenticationService;
import com.flowershop.bus.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class FlowerController implements FlowerOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetFlowersResult findByFilters(Double price,
                                           Integer limit,
                                           Integer offset) {
        return bus.executeQuery(GetFlowers.builder()
                .currentUsername(auth.currentUsername())
                .price(price)
                .limit(limit)
                .offset(offset)
                .build());
    }

    @Override
    public AddFlowerResult add(@Valid AddFlower command) {
        return bus.executeCommand(command.toBuilder().currentUsername(auth.currentUsername()).build());
    }

    @Override
    public GetFlowerResult findBySlug(String slug) {
        return bus.executeQuery(new GetFlower(auth.currentUsername(), slug));
    }

    @Override
    public UpdateFlowerResult updateBySlug(String slug, @Valid UpdateFlower command) {
        return bus.executeCommand(command.toBuilder().slug(slug).currentUsername(auth.currentUsername()).build());
    }

    @Override
    public void deleteBySlug(String slug) {
        bus.executeCommand(new DeleteFlower(auth.currentUsername(), slug));
    }

}
