package com.flowershop.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flowershop.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@Builder(toBuilder = true)
@Getter
@JsonRootName("shoppingCart")
@NoArgsConstructor
@AllArgsConstructor
public class AddToShoppingCart implements Command<AddToShoppingCartResult> {
    private String flowerSlug;
    @PositiveOrZero
    private Integer amount;
    private String currentUsername;
}
