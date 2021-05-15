 
package com.flowershop.application;

import com.flowershop.api.dto.UserDto;
import com.flowershop.application.service.JwtService;
import com.flowershop.domain.model.User;

public class UserAssembler {

    public static UserDto assemble(User user, JwtService jwtService) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(jwtService.getToken(user))
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

}
