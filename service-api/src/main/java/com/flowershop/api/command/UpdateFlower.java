package com.flowershop.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flowershop.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@JsonRootName("flower")
public class UpdateFlower implements Command<UpdateFlowerResult> {

    private String slug;
    private String name;
    private String image;
    private String description;
    private Double price;
    private String currentUsername;

}
