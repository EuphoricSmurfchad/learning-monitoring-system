package com.smurfchad.learning_monitoring_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smurfchad.learning_monitoring_system.dto.schedule.ReturnScheduleDto;
import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleDto;
import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleMapper;
import com.smurfchad.learning_monitoring_system.exception.BusinessConflictException;
import com.smurfchad.learning_monitoring_system.exception.InvalidRequestException;
import com.smurfchad.learning_monitoring_system.exception.ResourceNotFoundException;
import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Schedule;
import com.smurfchad.learning_monitoring_system.repository.CourseRepository;
import com.smurfchad.learning_monitoring_system.repository.GroupRepository;
import com.smurfchad.learning_monitoring_system.repository.ScheduleRepository;
import com.smurfchad.learning_monitoring_system.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ReturnScheduleDto addSchedule(ScheduleDto scheduleDto) {
        if (!scheduleDto.startTime().isBefore(scheduleDto.endTime())) {
            throw new InvalidRequestException("Неправильное время");
        }
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        Group group = groupRepository.findById(scheduleDto.groupId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Группа с id " + scheduleDto.groupId() + " не найдена"));
        Course course = courseRepository.findById(scheduleDto.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + scheduleDto.courseId() + " не найден"));
        List<Long> groupIds = course.getGroups().stream()
                .map(Group::getId)
                .toList();
        if (!groupIds.contains(group.getId())) {
            throw new BusinessConflictException("Группа не состоит на курсе");
        }
        schedule.setCourse(course);
        schedule.setGroup(group);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(savedSchedule);
    }

    @Transactional(readOnly = true)
    public ReturnScheduleDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Расписание с id " + id + " не найдено"));
        return scheduleMapper.toDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Расписание с id " + id + " не найдено"));
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public ReturnScheduleDto updateSchedule(Long id, ScheduleDto scheduleDto) {
        if (!scheduleDto.startTime().isBefore(scheduleDto.endTime())) {
            throw new InvalidRequestException("Неправильное время");
        }
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Расписание с id " + id + " не найдено"));
        Group group = groupRepository.findById(scheduleDto.groupId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Группа с id " + scheduleDto.groupId() + " не найдена"));
        Course course = courseRepository.findById(scheduleDto.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс с id " + scheduleDto.courseId() + " не найден"));
        List<Long> groupIds = course.getGroups().stream()
                .map(Group::getId)
                .toList();
        if (!groupIds.contains(group.getId())) {
            throw new BusinessConflictException("Группа не состоит на курсе");
        }
        schedule.setGroup(group);
        schedule.setCourse(course);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(savedSchedule);

    }

    @Transactional(readOnly = true)
    public Page<ReturnScheduleDto> findAllByGroupId(Long id, Pageable pageable) {
        groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + id + " не найдена"));
        Page<Schedule> schedules = scheduleRepository.findAllByGroupId(id, pageable);
        return schedules.map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ReturnScheduleDto> findAllByCourseTeacherId(Long id, Pageable pageable) {
        teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель с id " + id + " не найден"));
        Page<Schedule> schedules = scheduleRepository.findAllByCourseTeacherId(id, pageable);
        return schedules.map(scheduleMapper::toDto);
    }

}
