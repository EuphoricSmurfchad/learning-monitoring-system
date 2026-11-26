package com.smurfchad.learning_monitoring_system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smurfchad.learning_monitoring_system.dto.student.ReturnStudentDto;
import com.smurfchad.learning_monitoring_system.dto.student.StudentDto;
import com.smurfchad.learning_monitoring_system.dto.student.StudentMapper;
import com.smurfchad.learning_monitoring_system.exception.BusinessConflictException;
import com.smurfchad.learning_monitoring_system.exception.InvalidRequestException;
import com.smurfchad.learning_monitoring_system.exception.ResourceNotFoundException;
import com.smurfchad.learning_monitoring_system.model.Group;
import com.smurfchad.learning_monitoring_system.model.Student;
import com.smurfchad.learning_monitoring_system.repository.GroupRepository;
import com.smurfchad.learning_monitoring_system.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupRepository groupRepository;

    @Transactional
    public ReturnStudentDto addStudent(StudentDto studentDto) {
        Student student = studentMapper.toEntity(studentDto);
        Set<Long> idsInSet = studentDto.groupIds().stream()
                .collect(Collectors.toSet());
        List<Group> foundGroups = groupRepository.findAllById(idsInSet);
        if (foundGroups.size() != idsInSet.size() || foundGroups.isEmpty()) {
            throw new InvalidRequestException("Некорректные id групп");
        }
        foundGroups.forEach(student::addGroup);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
        studentRepository.deleteById(id);
    }

    @Transactional
    public ReturnStudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
        student.setFirstName(studentDto.firstName());
        student.setLastName(studentDto.lastName());
        Set<Long> idsInSet = studentDto.groupIds().stream()
                .collect(Collectors.toSet());
        List<Group> foundGroups = groupRepository.findAllById(idsInSet);
        if (foundGroups.size() != idsInSet.size() || foundGroups.isEmpty()) {
            throw new InvalidRequestException("Некорректные id групп");
        }
        Set<Group> groupsCopy = new HashSet<>(student.getGroups());
        groupsCopy.forEach(student::removeGroup);
        foundGroups.forEach(student::addGroup);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional(readOnly = true)
    public ReturnStudentDto getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
        return studentMapper.toDto(student);
    }

    @Transactional
    public ReturnStudentDto addStudentAtGroup(Long studentId, Long groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + studentId + " не найден"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
        List<Long> ids = group.getStudents().stream()
                .map(Student::getId)
                .toList();
        if (ids.contains(student.getId())) {
            throw new BusinessConflictException("Студент уже в группе");
        }
        student.addGroup(group);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public ReturnStudentDto removeStudentFromGroup(Long studentId, Long groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + studentId + " не найден"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа с id " + groupId + " не найдена"));
        List<Long> ids = group.getStudents().stream()
                .map(Student::getId)
                .toList();
        if (!ids.contains(student.getId())) {
            throw new ResourceNotFoundException("Студент не найден в группе");
        }
        if (student.getGroups().size() == 1) {
            throw new BusinessConflictException("Последняя группа у студента");
        }
        student.removeGroup(group);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

}
