package com.resitplatform.application;

import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.domain.model.Resit;

public class ResitAssembler {
    public static ResitDto assemble(Resit resit) {
        return ResitDto.builder()
                .slug(resit.getSlug())
                .teacherName(resit.getResponsibleTeacher().getUsername())
                .name(resit.getName())
                .description(resit.getDescription())
                .image(resit.getImage())
                .build();
    }

}
