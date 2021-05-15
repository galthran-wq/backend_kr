package com.flowershop.api.query;

import com.flowershop.api.dto.FlowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetFlowerResult {
    private FlowerDto flower;
}
