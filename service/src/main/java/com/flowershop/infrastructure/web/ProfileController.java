 
package com.flowershop.infrastructure.web;

import com.flowershop.api.operation.ProfileOperations;
import com.flowershop.api.query.GetProfile;
import com.flowershop.api.query.GetProfileResult;
import com.flowershop.bus.Bus;
import com.flowershop.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.version}")
public class ProfileController implements ProfileOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetProfileResult findByUsername(String username) {
        return bus.executeQuery(new GetProfile(auth.currentUsername(), username));
    }

}
