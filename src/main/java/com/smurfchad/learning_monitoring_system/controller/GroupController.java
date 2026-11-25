package com.smurfchad.learning_monitoring_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smurfchad.learning_monitoring_system.dto.group.GroupDto;
import com.smurfchad.learning_monitoring_system.dto.group.ReturnGroupDto;

import com.smurfchad.learning_monitoring_system.service.GroupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<ReturnGroupDto> addGroup(@Valid @RequestBody GroupDto groupDto) {
        ReturnGroupDto returnGroupDto = groupService.addGroup(groupDto);
        URI location = URI.create("/api/v1/groups/" + returnGroupDto.id());
        return ResponseEntity.created(location).body(returnGroupDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnGroupDto> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroup(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnGroupDto> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupDto groupDto) {
        ReturnGroupDto returnGroupDto = groupService.updateGroup(id, groupDto);
        return ResponseEntity.ok(returnGroupDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
    }

    @PostMapping("/{groupId}/courses/{courseId}")
    public ResponseEntity<ReturnGroupDto> addGroupAtCourse(@PathVariable Long groupId, @PathVariable Long courseId) {
        ReturnGroupDto returnGroupDto = groupService.addGroupAtCourse(groupId, courseId);
        return ResponseEntity.ok(returnGroupDto);

    }

    @DeleteMapping("/{groupId}/courses/{courseId}")
    public ResponseEntity<ReturnGroupDto> removeGroupFromCourse(@PathVariable Long groupId, @PathVariable Long courseId) {
        ReturnGroupDto returnGroupDto = groupService.removeGroupFromCourse(groupId, courseId);
        return ResponseEntity.ok(returnGroupDto);

    }
}
