package com.flowershop.api.query;

import com.flowershop.api.dto.ShoppingCartDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetShoppingCartResult {
    ShoppingCartDto shoppingCart;
}
