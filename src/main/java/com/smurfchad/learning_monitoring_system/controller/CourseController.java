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

import com.smurfchad.learning_monitoring_system.dto.course.CourseDtoResponse;
import com.smurfchad.learning_monitoring_system.dto.course.CourseDto;
import com.smurfchad.learning_monitoring_system.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1/courses")
@RequiredArgsConstructor 
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDtoResponse> addCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDtoResponse returnCourseDto = courseService.addCourse(courseDto);
        URI location = URI.create("/api/v1/courses/" + returnCourseDto.id());
        return ResponseEntity.created(location).body(returnCourseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDtoResponse> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDtoResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        CourseDtoResponse returnCourseDto = courseService.updateCourse(id, courseDto);
        return ResponseEntity.ok(returnCourseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
