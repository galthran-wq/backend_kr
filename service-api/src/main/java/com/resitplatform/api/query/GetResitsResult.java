package com.resitplatform.api.query;

import com.resitplatform.api.dto.FlowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetResitsResult {

    private List<FlowerDto> flowers;
    private Integer flowersCount;

}
