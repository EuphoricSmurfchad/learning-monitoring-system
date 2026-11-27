package com.smurfchad.learning_monitoring_system.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleDtoResponse;
import com.smurfchad.learning_monitoring_system.dto.schedule.ScheduleDto;
import com.smurfchad.learning_monitoring_system.service.ScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDtoResponse> addSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        ScheduleDtoResponse returnScheduleDto = scheduleService.addSchedule(scheduleDto);
        URI location = URI.create("/api/v1/schedules/" + returnScheduleDto.id());
        return ResponseEntity.created(location).body(returnScheduleDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDtoResponse> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDtoResponse> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleDto scheduleDto) {
        ScheduleDtoResponse returnScheduleDto = scheduleService.updateSchedule(id, scheduleDto);
        return ResponseEntity.ok(returnScheduleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<Page<ScheduleDtoResponse>> findAllByTeacherId(@PathVariable Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<ScheduleDtoResponse> schedules = scheduleService.findAllByCourseTeacherId(id, pageable);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<Page<ScheduleDtoResponse>> findAllByGroupId(@PathVariable Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());    
        Page<ScheduleDtoResponse> schedules = scheduleService.findAllByGroupId(id, pageable);
        return ResponseEntity.ok(schedules);
    }

}
