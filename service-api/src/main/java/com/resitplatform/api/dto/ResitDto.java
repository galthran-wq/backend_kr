package com.resitplatform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResitDto {
    private String slug;
    private String name;
    private String teacherName;
    private String image;
    private String description;
    private ProfileDto[] participants;
}
