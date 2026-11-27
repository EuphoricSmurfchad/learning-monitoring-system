package com.smurfchad.learning_monitoring_system.dto.group;

import jakarta.validation.constraints.NotBlank;

public record GroupDto(

    @NotBlank 
    String name
) {

}
