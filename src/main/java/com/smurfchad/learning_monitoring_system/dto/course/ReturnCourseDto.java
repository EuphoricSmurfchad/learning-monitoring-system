package com.smurfchad.learning_monitoring_system.dto.course;

public record ReturnCourseDto(
    Long id,
    String name,
    String description,
    Long teacherId
) {

}
