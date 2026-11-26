package com.smurfchad.learning_monitoring_system.dto.teacher;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "courseIds", source = "courses")
    TeacherDtoResponse toDto(Teacher teacher);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Teacher toEntity(TeacherDto teacherDto);

    default List<Long> mapCoursesToIds(Set<Course> courses) {
        return courses.stream()
                .filter(Objects::nonNull)
                .map(Course::getId)
                .toList();
    }
}
