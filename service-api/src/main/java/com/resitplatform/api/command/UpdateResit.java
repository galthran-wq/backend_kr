package com.resitplatform.api.command;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.resitplatform.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@JsonRootName("flower")
public class UpdateResit implements Command<UpdateResitResult> {

    private String slug;
    private String name;
    private String image;
    private String description;
    private String currentUsername;

}
