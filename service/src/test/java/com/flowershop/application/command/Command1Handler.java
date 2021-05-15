
package com.flowershop.application.command;

import com.flowershop.bus.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class Command1Handler implements CommandHandler<String, Command1> {

    @Override
    public String handle(Command1 command) {
        return null;
    }
}
