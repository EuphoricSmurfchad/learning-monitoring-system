package com.smurfchad.learning_monitoring_system.dto.group;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Student;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Group toEntity(GroupDto groupDto);

    @Mapping(target = "studentIds", source = "students")
    @Mapping(target = "courseIds", source = "courses")
    GroupDtoResponse toDto(Group group);

    default List<Long> mapStudentsToIds(Set<Student> students) {
        return students.stream()
                .filter(Objects::nonNull)
                .map(Student::getId)
                .toList();
    }

    default List<Long> mapCoursesToIds(Set<Course> courses) {
        return courses.stream()
                .filter(Objects::nonNull)
                .map(Course::getId)
                .toList();
    }

}
