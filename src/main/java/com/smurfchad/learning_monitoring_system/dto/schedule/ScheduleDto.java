package com.smurfchad.learning_monitoring_system.dto.schedule;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ScheduleDto(
    @NotNull 
    Long groupId,

    @NotNull 
    Long courseId,

    @NotNull 
    LocalDateTime startTime,

    @NotNull 
    LocalDateTime endTime
) {

}
