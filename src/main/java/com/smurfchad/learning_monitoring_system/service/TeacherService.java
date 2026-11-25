package com.smurfchad.learning_monitoring_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.smurfchad.learning_monitoring_system.dto.teacher.ReturnTeacherDto;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDto;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherMapper;
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
    public ReturnTeacherDto addTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Transactional(readOnly = true)
    public ReturnTeacherDto getTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return teacherMapper.toDto(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (courseRepository.existsByTeacherId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        teacherRepository.deleteById(id);
    }

    @Transactional 
    public ReturnTeacherDto updateTeacher(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        teacher.setFirstName(teacherDto.firstName());
        teacher.setLastName(teacherDto.lastName());
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }
}
