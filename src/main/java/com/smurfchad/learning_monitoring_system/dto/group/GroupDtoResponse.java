package com.smurfchad.learning_monitoring_system.dto.group;

import java.util.List;

public record GroupDtoResponse(
    Long id,
    String name,
    List<Long> studentIds,
    List<Long> courseIds
) {

}
