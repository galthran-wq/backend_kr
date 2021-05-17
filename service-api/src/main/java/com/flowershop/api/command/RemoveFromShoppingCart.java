package com.flowershop.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flowershop.bus.Command;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;

@Builder(toBuilder = true)
@Getter
public class RemoveFromShoppingCart implements Command<RemoveFromShoppingCartResult> {
    private String flowerSlug;
    @PositiveOrZero
    private Integer amountToRemove;
    private String currentUsername;
}
