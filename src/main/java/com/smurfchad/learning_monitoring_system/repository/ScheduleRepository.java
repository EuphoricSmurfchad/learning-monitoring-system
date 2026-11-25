package com.smurfchad.learning_monitoring_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smurfchad.learning_monitoring_system.model.Schedule;

@Repository 
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    boolean existsByCourseId(Long courseId);

    boolean existsByGroupId(long groupId);

    List<Schedule> findAllByGroupId(Long id);

    List<Schedule> findAllByCourseTeacherId(Long id);
}
