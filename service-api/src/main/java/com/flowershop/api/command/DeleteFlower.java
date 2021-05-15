package com.flowershop.api.command;

import com.flowershop.bus.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteFlower implements Command<DeleteFlowerResult> {
    private String currentUsername;
    private String slug;
}
