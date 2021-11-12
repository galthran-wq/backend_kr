 
package com.resitplatform.rest.auth;

import com.resitplatform.api.command.LoginUser;
import com.resitplatform.api.command.LoginUserResult;
import com.resitplatform.api.command.RegisterUser;
import com.resitplatform.api.operation.UserClient;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
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

    // todo is_teacher optional false by default
    public RegisteredUser register(String username, String email, String password, Boolean isTeacher) {
        userClient.register(RegisterUser.builder()
                .username(username)
                .email(email)
                .password(password)
                .isTeacher(isTeacher)
                .build());
        return new RegisteredUser(email, username, password);
    }

    public RegisteredUser register(String username, String email, String password) {
        return register(username, email, password, Boolean.FALSE);
    }

    public RegisteredUser registerTeacher() {
        String uuid = UUID.randomUUID().toString();
        return registerTeacher(uuid, email(uuid), uuid);
    }

    public RegisteredUser registerTeacher(String username, String email, String password) {
//        RegisteredUser userDto = register(username, email, password);
//        User user = userRepository.findByUsername(userDto.getUsername())
//                .orElseThrow();
//        userRepository.save(user
//                .toBuilder()
//                .is_teacher(Boolean.TRUE)
//                .build()
//        );
//        return userDto;
        return register(username, email, password, Boolean.TRUE);
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
