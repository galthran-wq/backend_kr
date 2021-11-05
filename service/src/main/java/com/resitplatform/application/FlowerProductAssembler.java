package com.resitplatform.application;

import com.resitplatform.api.dto.FlowerProductDto;
import com.resitplatform.domain.model.Flower;
import com.resitplatform.domain.model.FlowerProduct;

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
