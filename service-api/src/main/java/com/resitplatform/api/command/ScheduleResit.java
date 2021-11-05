package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Builder(toBuilder = true)
@Getter
@JsonRootName("flower")
public class ScheduleResit implements Command<ScheduleResitResult> {
    @NotBlank
    private String name;
    @PositiveOrZero
    private Double price;
    private String image;
    private String description;
    @PositiveOrZero
    private Integer availableAmount;
    private String currentUsername;


}
