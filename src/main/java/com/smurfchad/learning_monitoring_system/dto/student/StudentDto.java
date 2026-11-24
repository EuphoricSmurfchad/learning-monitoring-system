package com.smurfchad.learning_monitoring_system.dto.student;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StudentDto(
    @NotBlank 
    String firstName,
    
    @NotBlank
    String lastName,

    @NotEmpty
    List<@NotNull Long> groupIds
) {

}
