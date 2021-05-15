package com.flowershop.api.query;

import com.flowershop.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetFlower implements Query<GetFlowerResult> {
    private String currentUsername;
    private String slug;
}
