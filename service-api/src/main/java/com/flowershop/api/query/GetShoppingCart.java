package com.flowershop.api.query;

import com.flowershop.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetShoppingCart implements Query<GetShoppingCartResult> {
    private String currentUsername;
}
