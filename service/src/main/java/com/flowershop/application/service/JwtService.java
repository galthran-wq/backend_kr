
package com.flowershop.application.service;

import com.flowershop.domain.model.User;

public interface JwtService {

    String getSubject(String token);

    String getToken(User user);

}
