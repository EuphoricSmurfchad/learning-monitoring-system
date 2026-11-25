package com.smurfchad.learning_monitoring_system.dto.teacher;

import jakarta.validation.constraints.NotBlank;

public record TeacherDto(
    @NotBlank 
    String firstName,

    @NotBlank 
    String lastName
) {

}
