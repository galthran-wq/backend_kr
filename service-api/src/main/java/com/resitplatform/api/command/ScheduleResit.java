package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Builder(toBuilder = true)
@Getter
@JsonRootName("resit")
public class ScheduleResit implements Command<ScheduleResitResult> {
    @NotBlank
    private String name;
    @NotBlank
    private String image;
//    @NotBlank
//    private
    private String description;
    private String currentUsername;


}
