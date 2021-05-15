 
package com.flowershop.api.query;

import com.flowershop.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCurrentUser implements Query<GetCurrentUserResult> {

    private String username;

}
