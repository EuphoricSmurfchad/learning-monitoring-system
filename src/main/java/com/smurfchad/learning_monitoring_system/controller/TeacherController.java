package com.smurfchad.learning_monitoring_system.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smurfchad.learning_monitoring_system.dto.teacher.ReturnTeacherDto;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDto;
import com.smurfchad.learning_monitoring_system.service.TeacherService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1/teachers")
@RequiredArgsConstructor 
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<ReturnTeacherDto> addTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        ReturnTeacherDto returnTeacherDto = teacherService.addTeacher(teacherDto);
        URI location = URI.create("/api/v1/teachers/" + returnTeacherDto.id());
        return ResponseEntity.created(location).body(returnTeacherDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnTeacherDto> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacher(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnTeacherDto> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        ReturnTeacherDto returnTeacherDto = teacherService.updateTeacher(id, teacherDto);
        return ResponseEntity.ok(returnTeacherDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }
}
