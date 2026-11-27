package com.smurfchad.learning_monitoring_system.dto.teacher;

import java.util.List;

public record TeacherDtoResponse(
    Long id,
    String firstName,
    String lastName,
    List<Long> courseIds
) {

}
