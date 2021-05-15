 
package com.flowershop.application.command;

import com.flowershop.api.command.LoginUser;
import com.flowershop.api.command.LoginUserResult;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.exception.UnauthorizedException;
import com.flowershop.application.service.JwtService;
import com.flowershop.bus.CommandHandler;
import com.flowershop.application.UserAssembler;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class LoginUserHandler implements CommandHandler<LoginUserResult, LoginUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public LoginUserResult handle(LoginUser command) {
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> BadRequestException.badRequest("user [email=%s] does not exist", command.getEmail()));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw UnauthorizedException.unauthorized("user [email=%s] password is incorrect", command.getEmail());
        }

        return new LoginUserResult(UserAssembler.assemble(user, jwtService));
    }

}
