 
package com.flowershop.api.command;

import com.flowershop.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginUserResult {

    private UserDto user;

}
