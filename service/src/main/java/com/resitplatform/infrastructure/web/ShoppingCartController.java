package com.resitplatform.infrastructure.web;

import com.resitplatform.api.operation.ShoppingCartOperations;
import com.resitplatform.api.query.GetShoppingCart;
import com.resitplatform.api.query.GetShoppingCartResult;
import com.resitplatform.application.service.AuthenticationService;
import com.resitplatform.bus.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class ShoppingCartController implements ShoppingCartOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetShoppingCartResult get() {
        return bus.executeQuery(GetShoppingCart.builder()
                .currentUsername(auth.currentUsername())
                .build());
    }

    @Override
    public AddToShoppingCartResult add(@Valid AddToShoppingCart command) {
        return bus.executeCommand(command.toBuilder()
                .currentUsername(auth.currentUsername())
                .build());
    }

    @Override
    public RemoveFromShoppingCartResult remove(@Valid RemoveFromShoppingCart command) {
        return bus.executeCommand(command.toBuilder()
                .currentUsername(auth.currentUsername())
                .build());
    }

    @Override
    public void checkout() {
        bus.executeCommand(new ShoppingCartCheckout(auth.currentUsername()));
    }
}