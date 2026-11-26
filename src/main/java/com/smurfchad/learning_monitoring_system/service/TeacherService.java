package com.smurfchad.learning_monitoring_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDtoResponse;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDto;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherMapper;
import com.smurfchad.learning_monitoring_system.exception.BusinessException;
import com.smurfchad.learning_monitoring_system.exception.ResourceNotFoundException;
import com.smurfchad.learning_monitoring_system.model.Teacher;
import com.smurfchad.learning_monitoring_system.repository.CourseRepository;
import com.smurfchad.learning_monitoring_system.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final CourseRepository courseRepository;

    @Transactional
    public TeacherDtoResponse addTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Transactional(readOnly = true)
    public TeacherDtoResponse getTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель с id " + id + " не найден"));
        return teacherMapper.toDto(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель с id " + id + " не найден"));
        if (courseRepository.existsByTeacherId(id)) {
            throw new BusinessException("У учителя есть курсы");
        }
        teacherRepository.deleteById(id);
    }

    @Transactional
    public TeacherDtoResponse updateTeacher(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель с id " + id + " не найден"));
        teacher.setFirstName(teacherDto.firstName());
        teacher.setLastName(teacherDto.lastName());
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }
}
