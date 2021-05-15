 
package com.flowershop.application.command;

import com.flowershop.api.command.UpdateUser;
import com.flowershop.api.command.UpdateUserResult;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.exception.NotFoundException;
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
public class UpdateUserHandler implements CommandHandler<UpdateUserResult, UpdateUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public UpdateUserResult handle(UpdateUser command) {
        User user = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> NotFoundException.notFound("user [name=%s] does not exist", command.getCurrentUsername()));

        if (command.getUsername() != null
                && !command.getUsername().equals(user.getUsername())
                && userRepository.findByUsername(command.getUsername()).isPresent()) {
            throw BadRequestException.badRequest("user [name=%s] already exists", command.getUsername());
        }

        if (command.getEmail() != null
                && !command.getEmail().equals(user.getEmail())
                && userRepository.findByEmail(command.getEmail()).isPresent()) {
            throw BadRequestException.badRequest("user [email=%s] already exists", command.getEmail());
        }

        User alteredUser = user.toBuilder()
                .email(command.getEmail() != null ? command.getEmail() : user.getEmail())
                .username(command.getUsername() != null ? command.getUsername() : user.getUsername())
                .password(command.getPassword() != null ? encoder.encode(command.getPassword()) : user.getPassword())
                .bio(command.getBio() != null ? command.getBio() : user.getBio())
                .image(command.getImage() != null ? command.getImage() : user.getImage())
                .build();

        User savedUser = userRepository.save(alteredUser);

        return new UpdateUserResult(UserAssembler.assemble(savedUser, jwtService));
    }

}
