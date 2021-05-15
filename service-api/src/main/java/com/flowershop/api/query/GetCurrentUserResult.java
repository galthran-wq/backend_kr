 
package com.flowershop.api.query;

import com.flowershop.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCurrentUserResult {

    private UserDto user;

}
