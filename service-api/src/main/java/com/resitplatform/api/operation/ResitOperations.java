package com.resitplatform.api.operation;

import com.resitplatform.api.command.*;
import com.resitplatform.api.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ResitOperations {
    @GetMapping("/resits{?name,teacherName,limit,offset}")
    GetResitsResult findByFilters(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String teacherName,
                                  @RequestParam(defaultValue = "20") Integer limit,
                                  @RequestParam(defaultValue = "0") Integer offset);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/resits")
    ScheduleResitResult add(@Valid @RequestBody ScheduleResit command);

    @GetMapping("/resits/{slug}")
    GetResitResult findBySlug(@PathVariable("slug") String slug);

    @PutMapping("/resits/{slug}")
    UpdateResitResult updateBySlug(@PathVariable("slug") String slug, @Valid @RequestBody UpdateResit command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/resits/{slug}")
    void deleteBySlug(@PathVariable("slug") String slug);
}
