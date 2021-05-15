package com.flowershop.infrastructure.web;

import com.flowershop.api.operation.UserOperations;
import com.flowershop.api.query.GetCurrentUser;
import com.flowershop.api.query.GetCurrentUserResult;
import com.flowershop.bus.Bus;
import com.flowershop.api.command.LoginUser;
import com.flowershop.api.command.LoginUserResult;
import com.flowershop.api.command.RegisterUser;
import com.flowershop.api.command.RegisterUserResult;
import com.flowershop.api.command.UpdateUser;
import com.flowershop.api.command.UpdateUserResult;
import com.flowershop.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class UserController implements UserOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public LoginUserResult login(@Valid LoginUser cmd) {
        return bus.executeCommand(cmd);
    }

    @Override
    public RegisterUserResult register(@Valid RegisterUser cmd) {
        return bus.executeCommand(cmd);
    }

    @Override
    public GetCurrentUserResult current() {
        return bus.executeQuery(new GetCurrentUser(auth.currentUsername()));
    }

    @Override
    public UpdateUserResult update(@Valid UpdateUser cmd) {
        return bus.executeCommand(cmd.withCurrentUsername(auth.currentUsername()));
    }

}
