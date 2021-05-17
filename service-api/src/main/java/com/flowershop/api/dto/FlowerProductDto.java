package com.flowershop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FlowerProductDto {
    FlowerDto flower;
    Integer totalAmount;
    Double totalProductPrice;
}
