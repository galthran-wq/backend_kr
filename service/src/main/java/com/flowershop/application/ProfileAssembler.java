 
package com.flowershop.application;

import com.flowershop.api.dto.ProfileDto;
import com.flowershop.domain.model.User;

public class ProfileAssembler {

    public static ProfileDto assemble(User user) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImage());
    }

}
