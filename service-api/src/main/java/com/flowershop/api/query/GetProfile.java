 
package com.flowershop.api.query;

import com.flowershop.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetProfile implements Query<GetProfileResult> {

    private String currentUsername;
    private String username;

}
