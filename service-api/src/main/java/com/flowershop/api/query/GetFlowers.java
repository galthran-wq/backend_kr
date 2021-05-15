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
public class GetFlowers implements Query<GetFlowersResult> {

    private Double price;
    private Integer limit;
    private Integer offset;
    private String currentUsername;

}
