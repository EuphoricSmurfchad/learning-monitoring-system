package com.smurfchad.learning_monitoring_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smurfchad.learning_monitoring_system.dto.student.ReturnStudentDto;
import com.smurfchad.learning_monitoring_system.dto.student.StudentDto;
import com.smurfchad.learning_monitoring_system.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ReturnStudentDto> addStudent(@Valid @RequestBody StudentDto studentDto) {
        ReturnStudentDto returnStudentDto = studentService.addStudent(studentDto);
        URI location = URI.create("/api/v1/students/" + returnStudentDto.id());
        return ResponseEntity.created(location).body(returnStudentDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnStudentDto> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnStudentDto> updateStudent(@PathVariable Long id,@Valid @RequestBody StudentDto studentDto) {
        ReturnStudentDto returnStudentDto = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(returnStudentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PostMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity<ReturnStudentDto> addStudentAtGroup(@PathVariable Long studentId, @PathVariable Long groupId) {
        ReturnStudentDto returnStudentDto = studentService.addStudentAtGroup(studentId, groupId);
        return ResponseEntity.ok(returnStudentDto);

    }

    @DeleteMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity<ReturnStudentDto> removeStudentFromGroup(@PathVariable Long studentId, @PathVariable Long groupId) {
        ReturnStudentDto returnStudentDto = studentService.removeStudentFromGroup(studentId, groupId);
        return ResponseEntity.ok(returnStudentDto);
    }

}
