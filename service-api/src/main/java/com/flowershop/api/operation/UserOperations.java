 
package com.flowershop.api.operation;

import com.flowershop.api.command.LoginUser;
import com.flowershop.api.command.LoginUserResult;
import com.flowershop.api.command.RegisterUser;
import com.flowershop.api.command.RegisterUserResult;
import com.flowershop.api.command.UpdateUser;
import com.flowershop.api.command.UpdateUserResult;
import com.flowershop.api.query.GetCurrentUserResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface UserOperations {

    @PostMapping("/users/login")
    LoginUserResult login(@Valid @RequestBody LoginUser command);

    @PostMapping("/users")
    RegisterUserResult register(@Valid @RequestBody RegisterUser command);

    @GetMapping("/user")
    GetCurrentUserResult current();

    @PutMapping("/user")
    UpdateUserResult update(@Valid @RequestBody UpdateUser command);

}
