package com.smurfchad.learning_monitoring_system.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.smurfchad.learning_monitoring_system.dto.schedule.ReturnScheduleDto;
import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleDto;
import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleMapper;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        Group group = groupRepository.findById(scheduleDto.groupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(scheduleDto.courseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Long> groupIds = course.getGroups().stream()
                .map(Group::getId)
                .toList();
        if (!groupIds.contains(group.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        schedule.setCourse(course);
        schedule.setGroup(group);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(savedSchedule);
    }

    @Transactional(readOnly = true)
    public ReturnScheduleDto getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return scheduleMapper.toDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public ReturnScheduleDto updateSchedule(Long id, ScheduleDto scheduleDto) {
        if (!scheduleDto.startTime().isBefore(scheduleDto.endTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Group group = groupRepository.findById(scheduleDto.groupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(scheduleDto.courseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Long> groupIds = course.getGroups().stream()
                .map(Group::getId)
                .toList();
        if (!groupIds.contains(group.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        schedule.setGroup(group);
        schedule.setCourse(course);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(savedSchedule);

    }

    @Transactional(readOnly = true)
    public List<ReturnScheduleDto> findAllByGroupId(Long id) {
        groupRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Schedule> schedules = scheduleRepository.findAllByGroupId(id);
        return schedules.stream().map(scheduleMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ReturnScheduleDto> findAllByCourseTeacherId(Long id) {
        teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Schedule> schedules = scheduleRepository.findAllByCourseTeacherId(id);
        return schedules.stream().map(scheduleMapper::toDto).toList();
    }

}
