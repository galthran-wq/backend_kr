package com.flowershop.api.command;

import com.flowershop.api.dto.ShoppingCartDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddToShoppingCartResult {
    private ShoppingCartDto shoppingCart;
}
