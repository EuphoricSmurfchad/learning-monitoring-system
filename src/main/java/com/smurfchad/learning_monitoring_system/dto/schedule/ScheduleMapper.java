package com.smurfchad.learning_monitoring_system.dto.schedule;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Schedule;

@Mapper (componentModel = "spring")
public interface ScheduleMapper {

    @Mapping (target = "groupId", source = "group")
    @Mapping (target = "courseId", source = "course")
    public ReturnScheduleDto toDto(Schedule schedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "course", ignore = true)
    public Schedule toEntity(ScheduleDto scheduleDto);

    default Long mapGroupToId(Group group){
        return group.getId();
    }

    default Long mapCoursesToIds(Course course){
        return course.getId();
    }

}
