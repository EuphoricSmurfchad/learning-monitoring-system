package com.smurfchad.learning_monitoring_system.dto.course;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Teacher;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping (target = "id", ignore = true)
    @Mapping (target = "teacher", ignore = true)
    @Mapping (target = "groups", ignore = true)
    @Mapping (target = "schedules", ignore = true)
    Course toEntity(CourseDto courseDto);

    @Mapping (target = "teacherId", source = "teacher")
    CourseDtoResponse toDto(Course course);

    default Long mapTeacherToId(Teacher teacher){
        return teacher.getId();
    }

}
