package com.smurfchad.learning_monitoring_system.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.smurfchad.learning_monitoring_system.dto.course.CourseDto;
import com.smurfchad.learning_monitoring_system.dto.course.CourseMapper;
import com.smurfchad.learning_monitoring_system.dto.course.ReturnCourseDto;
import com.smurfchad.learning_monitoring_system.model.Course;
import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Teacher;
import com.smurfchad.learning_monitoring_system.repository.CourseRepository;
import com.smurfchad.learning_monitoring_system.repository.ScheduleRepository;
import com.smurfchad.learning_monitoring_system.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ReturnCourseDto addCourse(CourseDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        Teacher teacher = teacherRepository.findById(courseDto.teacherId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        teacher.addCourse(course);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    @Transactional(readOnly = true)
    public ReturnCourseDto getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return courseMapper.toDto(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (scheduleRepository.existsByCourseId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Set<Group> coursesCopy = new HashSet<>(course.getGroups());
        coursesCopy.forEach(course::removeGroup);
        courseRepository.deleteById(id);
    }

    @Transactional
    public ReturnCourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        course.setName(courseDto.name());
        course.setDescription(courseDto.description());
        course.getTeacher().getCourses().remove(course);
        Teacher teacher = teacherRepository.findById(courseDto.teacherId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        teacher.addCourse(course);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

}
