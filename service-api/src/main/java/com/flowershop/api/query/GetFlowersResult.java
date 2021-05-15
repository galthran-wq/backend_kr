package com.flowershop.api.query;

import com.flowershop.api.dto.FlowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFlowersResult {

    private List<FlowerDto> flowers;
    private Integer flowersCount;

}
