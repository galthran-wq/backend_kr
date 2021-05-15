package com.flowershop.api.operation;

import com.flowershop.api.command.*;
import com.flowershop.api.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface FlowerOperations {
    @GetMapping("/flowers{?price,limit,offset}")
    GetFlowersResult findByFilters(@RequestParam(required = false) Double price,
                                  @RequestParam(defaultValue = "20") Integer limit,
                                  @RequestParam(defaultValue = "0") Integer offset);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/flowers")
    AddFlowerResult add(@Valid @RequestBody AddFlower command);

    @GetMapping("/flowers/{slug}")
    GetFlowerResult findBySlug(@PathVariable("slug") String slug);

    @PutMapping("/flowers/{slug}")
    UpdateFlowerResult updateBySlug(@PathVariable("slug") String slug, @Valid @RequestBody UpdateFlower command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/flowers/{slug}")
    void deleteBySlug(@PathVariable("slug") String slug);
}
