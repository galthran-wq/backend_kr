package com.resitplatform.application;

import com.resitplatform.api.dto.FlowerDto;
import com.resitplatform.domain.model.Flower;
import com.resitplatform.domain.model.Resit;

public class ResitAssembler {
    public static FlowerDto assemble(Resit flower) {
        return FlowerDto.builder()
                .slug(flower.getSlug())
                .name(flower.getName())
                .description(flower.getDescription())
                .price(flower.getPrice())
                .image(flower.getImage())
                .availableAmount(flower.getAvailableAmount())
                .build();
    }

}
