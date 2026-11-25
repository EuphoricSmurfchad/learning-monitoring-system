package com.smurfchad.learning_monitoring_system.dto.schedule;

import java.time.LocalDateTime;

public record ReturnScheduleDto(
    Long id,
    Long groupId,
    Long courseId,
    LocalDateTime startTime,
    LocalDateTime endTime
) {

}
