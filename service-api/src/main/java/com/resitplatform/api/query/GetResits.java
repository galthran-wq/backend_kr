package com.resitplatform.api.query;

import com.resitplatform.bus.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetResits implements Query<GetResitsResult> {

    // todo what is it? do we query by price?
    private Double price;
    private Integer limit;
    private Integer offset;
    private String currentUsername;

}
