package com.flowershop.api.command;

import com.flowershop.api.dto.FlowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddFlowerResult {
    private FlowerDto flower;
}
