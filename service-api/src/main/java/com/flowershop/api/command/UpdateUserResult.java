 
package com.flowershop.api.command;

import com.flowershop.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateUserResult {

    private UserDto user;

}
