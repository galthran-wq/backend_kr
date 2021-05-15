package com.flowershop.application;

import com.flowershop.api.dto.FlowerDto;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.User;

public class FlowerAssembler {
    public static FlowerDto assemble(Flower flower) {
        return FlowerDto.builder()
                .slug(flower.getSlug())
                .name(flower.getName())
                .description(flower.getDescription())
                .price(flower.getPrice())
                .image(flower.getImage())
                .build();
    }

}
