
package com.flowershop.rest;

import com.flowershop.api.command.LoginUser;
import com.flowershop.api.command.RegisterUser;
import com.flowershop.api.command.UpdateUser;
import com.flowershop.api.dto.UserDto;
import com.flowershop.api.operation.UserClient;
import com.flowershop.rest.auth.AuthSupport;
import com.flowershop.rest.support.FeignBasedRestTest;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class UserApiTest extends FeignBasedRestTest {

    public static final String ALTERED_EMAIL = "altered-email@example.com";
    public static final String ALTERED_USERNAME = "altered-username";
    public static final String ALTERED_PASSWORD = "altered-password";
    public static final String ALTERED_BIO = "altered-bio";
    public static final String ALTERED_IMAGE = "altered-image";

    @Autowired
    private AuthSupport auth;

    @Autowired
    private UserClient userClient;

    @Test
    void should_returnCorrectData_whenRegisterUser() {
        RegisterUser command = registerCommand();

        UserDto user = userClient.register(command).getUser();

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getToken()).isNotBlank();
    }

    @Test
    void should_returnCorrectData_whenLoginUser() {
        RegisterUser command = registerCommand();

        userClient.register(command);

        UserDto user = userClient.login(new LoginUser(command.getEmail(), command.getPassword())).getUser();

        assertThat(user.getToken()).isNotBlank();
    }

    @Test
    void should_returnCorrectData_whenLoginUserWithWrongPassword() {
        RegisterUser command = registerCommand();

        userClient.register(command);

        FeignException exception = catchThrowableOfType(
                () -> userClient.login(new LoginUser(command.getEmail(), UUID.randomUUID().toString())),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void should_return400_whenLoginUser() {
        String s = UUID.randomUUID().toString();
        FeignException exception = catchThrowableOfType(
                () -> userClient.login(new LoginUser(s + "@ex.com", s)),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void should_returnCorrectData_whenUpdateUser() {
        auth.register().login();

        UpdateUser updateUser = UpdateUser.builder()
                .email(ALTERED_EMAIL)
                .username(ALTERED_USERNAME)
                .password(ALTERED_PASSWORD)
                .bio(ALTERED_BIO)
                .image(ALTERED_IMAGE)
                .build();

        UserDto user = userClient.update(updateUser).getUser();

        assertThat(user.getEmail()).isEqualTo(ALTERED_EMAIL);
        assertThat(user.getUsername()).isEqualTo(ALTERED_USERNAME);
        assertThat(user.getBio()).isEqualTo(ALTERED_BIO);
        assertThat(user.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    @Test
    void should_throw400_whenUpdateUserWithExistingEmail() {
        AuthSupport.RegisteredUser registeredUser = auth.register();

        auth.register().login();

        UpdateUser updateUser = UpdateUser.builder()
                .email(registeredUser.getEmail())
                .build();

        FeignException exception = catchThrowableOfType(
                () -> userClient.update(updateUser),
                FeignException.class
        );

        assertThat(exception).isNotNull();
    }

    @Test
    void should_throw400_whenUpdateUserWithExistingName() {
        AuthSupport.RegisteredUser registeredUser = auth.register();

        auth.register().login();

        UpdateUser updateUser = UpdateUser.builder()
                .username(registeredUser.getUsername())
                .build();

        FeignException exception = catchThrowableOfType(
                () -> userClient.update(updateUser),
                FeignException.class
        );

        assertThat(exception).isNotNull();
    }

    private static RegisterUser registerCommand() {
        return RegisterUser.builder()
                .username(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString() + "@ex.com")
                .password(UUID.randomUUID().toString())
                .build();
    }

}
