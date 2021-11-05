package com.resitplatform.application;

import com.resitplatform.api.dto.FlowerProductDto;
import com.resitplatform.api.dto.ShoppingCartDto;
import com.resitplatform.domain.model.FlowerProduct;

import java.util.ArrayList;

public class ShoppingCartAssembler {

    public static ShoppingCartDto assemble(ShoppingCart cart) {
        ArrayList<FlowerProductDto> flowerProductDtos = new ArrayList<>();
        Double totalPrice = 0.0;

        for (FlowerProduct flowerProduct : cart.getFlowerProducts()) {
            flowerProductDtos.add(FlowerProductAssembler.assemble(flowerProduct));
            totalPrice += flowerProduct.getTotal();
        }

        return new ShoppingCartDto(
                flowerProductDtos.toArray(new FlowerProductDto[0]),
                totalPrice
        );
    }
}
