package com.smurfchad.learning_monitoring_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smurfchad.learning_monitoring_system.dto.group.GroupDto;
import com.smurfchad.learning_monitoring_system.dto.group.GroupMapper;
import com.smurfchad.learning_monitoring_system.dto.group.GroupDtoResponse;
import com.smurfchad.learning_monitoring_system.exception.BusinessException;
import com.smurfchad.learning_monitoring_system.exception.ResourceNotFoundException;
import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Group;

import com.smurfchad.learning_monitoring_system.repository.CourseRepository;
import com.smurfchad.learning_monitoring_system.repository.GroupRepository;
import com.smurfchad.learning_monitoring_system.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public GroupDtoResponse addGroup(GroupDto groupDto) {
        Group group = groupMapper.toEntity(groupDto);
        group.setName(groupDto.name());
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional(readOnly = true)
    public GroupDtoResponse getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
        return groupMapper.toDto(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
        if (!group.getStudents().isEmpty()) {
            throw new BusinessException("В группе есть студенты");
        }
        if (scheduleRepository.existsByGroupId(id)) {
            throw new BusinessException("У группы есть занятия");
        }
        groupRepository.deleteById(id);
    }

    @Transactional
    public GroupDtoResponse updateGroup(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
        group.setName(groupDto.name());
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional
    public GroupDtoResponse addGroupAtCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + courseId + " не найден"));
        List<Long> ids = group.getCourses().stream()
                .map(Course::getId)
                .toList();
        if (ids.contains(course.getId())) {
            throw new BusinessException("Группа уже на курсе");
        }
        course.addGroup(group);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional
    public GroupDtoResponse removeGroupFromCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + courseId + " не найден"));
        List<Long> ids = group.getCourses().stream()
                .map(Course::getId)
                .toList();
        if (!ids.contains(course.getId())) {
            throw new BusinessException("Группа не на курсе");
        }
        course.removeGroup(group);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }
}
