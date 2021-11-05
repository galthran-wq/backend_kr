package com.resitplatform.infrastructure.web;

import com.resitplatform.api.command.*;
import com.resitplatform.api.operation.FlowerOperations;
import com.resitplatform.api.query.GetResit;
import com.resitplatform.api.query.GetResitResult;
import com.resitplatform.api.query.GetResits;
import com.resitplatform.api.query.GetResitsResult;
import com.resitplatform.application.service.AuthenticationService;
import com.resitplatform.bus.Bus;
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
    public GetResitsResult findByFilters(Double price,
                                         Integer limit,
                                         Integer offset) {
        return bus.executeQuery(GetResits.builder()
                .currentUsername(auth.currentUsername())
                .price(price)
                .limit(limit)
                .offset(offset)
                .build());
    }

    @Override
    public ScheduleResitResult add(@Valid AddFlower command) {
        return bus.executeCommand(command.toBuilder().currentUsername(auth.currentUsername()).build());
    }

    @Override
    public GetResitResult findBySlug(String slug) {
        return bus.executeQuery(new GetResit(auth.currentUsername(), slug));
    }

    @Override
    public UpdateResitResult updateBySlug(String slug, @Valid UpdateResit command) {
        return bus.executeCommand(command.toBuilder().slug(slug).currentUsername(auth.currentUsername()).build());
    }

    @Override
    public void deleteBySlug(String slug) {
        bus.executeCommand(new CancelResit(auth.currentUsername(), slug));
    }

}
