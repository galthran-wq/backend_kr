package com.flowershop.api.operation;

import com.flowershop.api.command.*;
import com.flowershop.api.query.GetShoppingCartResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

public interface ShoppingCartOperations {

    @GetMapping("/cart")
    GetShoppingCartResult get();

    @PostMapping("/cart/add")
    AddToShoppingCartResult add(@Valid @RequestBody AddToShoppingCart command);

    @PostMapping("/cart/remove")
    RemoveFromShoppingCartResult remove(@Valid @RequestBody RemoveFromShoppingCart command);

    @PostMapping("/cart/checkout")
    void checkout();
}
