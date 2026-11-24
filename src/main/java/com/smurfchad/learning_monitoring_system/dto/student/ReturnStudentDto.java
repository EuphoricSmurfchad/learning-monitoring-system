package com.smurfchad.learning_monitoring_system.dto.student;

import java.util.List;

public record ReturnStudentDto(
    Long id,
    String firstName,
    String lastName,
    List<Long> groupIds
) {

}
