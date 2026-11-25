package com.smurfchad.learning_monitoring_system.dto.group;

import java.util.List;

public record ReturnGroupDto(
    Long id,
    String name,
    List<Long> studentIds,
    List<Long> courseIds
) {

}
