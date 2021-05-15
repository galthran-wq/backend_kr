 
package com.flowershop.rest.auth;

import com.flowershop.api.command.LoginUser;
import com.flowershop.api.command.LoginUserResult;
import com.flowershop.api.command.RegisterUser;
import com.flowershop.api.operation.UserClient;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthSupport {

    @Autowired(required = false)
    private UserClient userClient;

    @Autowired
    private UserRepository userRepository;

    public RegisteredUser register() {
        String uuid = UUID.randomUUID().toString();
        return register(uuid, email(uuid), uuid);
    }

    public RegisteredUser register(String username, String email, String password) {
        userClient.register(RegisterUser.builder()
                .username(username)
                .email(email)
                .password(password)
                .build());
        return new RegisteredUser(email, username, password);
    }

    public RegisteredUser registerOwner() {
        String uuid = UUID.randomUUID().toString();
        return registerOwner(uuid, email(uuid), uuid);
    }

    public RegisteredUser registerOwner(String username, String email, String password) {
        RegisteredUser userDto = register(username, email, password);
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow();
        userRepository.save(user
                .toBuilder()
                .is_owner(Boolean.TRUE)
                .build()
        );
        return userDto;

    }

    private String email(String uuid) {
        return uuid + "@ex.com";
    }

    public void login(String cred) {
        login(email(cred), cred);
    }

    public void login(String email, String password) {
        LoginUserResult result = userClient.login(new LoginUser(email, password));
        TokenHolder.token = result.getUser().getToken();
    }

    public void logout() {
        TokenHolder.token = null;
    }

    public static class TokenHolder {
        public static String token;
    }

    @AllArgsConstructor
    @Getter
    public class RegisteredUser {
        private String email;
        private String username;
        private String password;

        public RegisteredUser login() {
            AuthSupport.this.login(email, password);
            return this;
        }
    }

}
