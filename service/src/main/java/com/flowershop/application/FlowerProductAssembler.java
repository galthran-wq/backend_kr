package com.flowershop.application;

import com.flowershop.api.dto.FlowerProductDto;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.FlowerProduct;

public class FlowerProductAssembler {

    public static FlowerProductDto assemble(FlowerProduct flowerProduct) {
        Flower flower = flowerProduct.getFlower();
        return new FlowerProductDto(
                FlowerAssembler.assemble(flower),
                flowerProduct.getAmount(),
                flowerProduct.getAmount() * flower.getPrice()
        );
    }

}
