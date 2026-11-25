package com.smurfchad.learning_monitoring_system.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseDto(
    @NotBlank 
    String name,
 
    String description,

    @NotNull 
    Long teacherId
) {

}
