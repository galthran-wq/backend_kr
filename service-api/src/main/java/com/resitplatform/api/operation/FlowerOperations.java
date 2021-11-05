package com.resitplatform.api.operation;

import com.resitplatform.api.command.*;
import com.resitplatform.api.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface FlowerOperations {
    @GetMapping("/flowers{?price,limit,offset}")
    GetResitsResult findByFilters(@RequestParam(required = false) Double price,
                                  @RequestParam(defaultValue = "20") Integer limit,
                                  @RequestParam(defaultValue = "0") Integer offset);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/flowers")
    ScheduleResitResult add(@Valid @RequestBody ScheduleResit command);

    @GetMapping("/flowers/{slug}")
    GetResitResult findBySlug(@PathVariable("slug") String slug);

    @PutMapping("/flowers/{slug}")
    UpdateResitResult updateBySlug(@PathVariable("slug") String slug, @Valid @RequestBody UpdateResit command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/flowers/{slug}")
    void deleteBySlug(@PathVariable("slug") String slug);
}
