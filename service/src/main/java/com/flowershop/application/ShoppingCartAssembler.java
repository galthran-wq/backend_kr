package com.flowershop.application;

import com.flowershop.api.dto.FlowerProductDto;
import com.flowershop.api.dto.ShoppingCartDto;
import com.flowershop.domain.model.FlowerProduct;
import com.flowershop.domain.model.ShoppingCart;

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
