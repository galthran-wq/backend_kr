package com.resitplatform.api.query;

import com.resitplatform.api.dto.ResitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetResitsResult {

    private List<ResitDto> resits;
    private Integer resitsCount;

}
