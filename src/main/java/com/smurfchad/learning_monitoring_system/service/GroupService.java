package com.smurfchad.learning_monitoring_system.service;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.smurfchad.learning_monitoring_system.dto.group.GroupDto;
import com.smurfchad.learning_monitoring_system.dto.group.GroupMapper;
import com.smurfchad.learning_monitoring_system.dto.group.ReturnGroupDto;

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
    public ReturnGroupDto addGroup(GroupDto groupDto) {
        Group group = groupMapper.toEntity(groupDto);
        group.setName(groupDto.name());
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional(readOnly = true)
    public ReturnGroupDto getGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return groupMapper.toDto(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!group.getStudents().isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (scheduleRepository.existsByGroupId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        groupRepository.deleteById(id);
    }

    @Transactional
    public ReturnGroupDto updateGroup(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        group.setName(groupDto.name());
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional
    public ReturnGroupDto addGroupAtCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Long> ids = group.getCourses().stream()
                .map(Course::getId)
                .toList();
        if (ids.contains(course.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        course.addGroup(group);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }

    @Transactional
    public ReturnGroupDto removeGroupFromCourse(Long groupId, Long courseId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Long> ids = group.getCourses().stream()
                .map(Course::getId)
                .toList();
        if (!ids.contains(course.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        course.removeGroup(group);
        Group savedGroup = groupRepository.save(group);
        return groupMapper.toDto(savedGroup);
    }
}
