package com.flowershop.api.command;

import com.flowershop.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartCheckout implements Command<ShoppingCartCheckoutResult> {
    private String currentUsername;
}
