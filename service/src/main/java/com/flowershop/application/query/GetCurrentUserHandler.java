 
package com.flowershop.application.query;

import com.flowershop.api.query.GetCurrentUser;
import com.flowershop.api.query.GetCurrentUserResult;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.service.JwtService;
import com.flowershop.bus.QueryHandler;
import com.flowershop.application.UserAssembler;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class GetCurrentUserHandler implements QueryHandler<GetCurrentUserResult, GetCurrentUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    @Override
    public GetCurrentUserResult handle(GetCurrentUser query) {
        User user = userRepository.findByUsername(query.getUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", query.getUsername()));

        return new GetCurrentUserResult(UserAssembler.assemble(user, jwtService));
    }

}
