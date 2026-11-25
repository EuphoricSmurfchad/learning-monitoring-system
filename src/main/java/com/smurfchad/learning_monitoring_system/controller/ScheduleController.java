package com.smurfchad.learning_monitoring_system.controller;

import java.net.URI;
import java.util.List;

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

import com.smurfchad.learning_monitoring_system.dto.schedule.ReturnScheduleDto;
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
    public ResponseEntity<ReturnScheduleDto> addSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        ReturnScheduleDto returnScheduleDto = scheduleService.addSchedule(scheduleDto);
        URI location = URI.create("/api/v1/schedules/" + returnScheduleDto.id());
        return ResponseEntity.created(location).body(returnScheduleDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnScheduleDto> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getSchedule(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnScheduleDto> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleDto scheduleDto) {
        ReturnScheduleDto returnScheduleDto = scheduleService.updateSchedule(id, scheduleDto);
        return ResponseEntity.ok(returnScheduleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<List<ReturnScheduleDto>> findAllByTeacherId(@PathVariable Long id) {
        List<ReturnScheduleDto> schedules = scheduleService.findAllByCourseTeacherId(id);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<List<ReturnScheduleDto>> findAllByGroupId(@PathVariable Long id) {
        List<ReturnScheduleDto> schedules = scheduleService.findAllByGroupId(id);
        return ResponseEntity.ok(schedules);
    }

}
