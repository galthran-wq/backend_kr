 
package com.flowershop.application.query;

import com.flowershop.api.query.GetProfile;
import com.flowershop.api.query.GetProfileResult;
import com.flowershop.application.exception.NotFoundException;
import com.flowershop.bus.QueryHandler;
import com.flowershop.application.ProfileAssembler;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetProfileHandler implements QueryHandler<GetProfileResult, GetProfile> {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public GetProfileResult handle(GetProfile query) {

        User user = userRepository.findByUsername(query.getUsername())
                .orElseThrow(() -> NotFoundException.notFound("user [name=%s] does not exist", query.getUsername()));

        return new GetProfileResult(ProfileAssembler.assemble(user));
    }

}
