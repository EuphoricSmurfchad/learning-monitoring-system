package com.smurfchad.learning_monitoring_system.dto.student;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "groupIds", source = "groups")
    ReturnStudentDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", ignore = true)
    Student toEntity(StudentDto studentDto);

    default List<Long> mapGroupsToIds(Set<Group> groups) {
        return groups.stream()
                .filter(Objects::nonNull)
                .map(Group::getId)
                .toList();
    }

}
