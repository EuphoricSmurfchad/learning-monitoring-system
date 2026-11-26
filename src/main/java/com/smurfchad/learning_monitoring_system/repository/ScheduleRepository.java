package com.smurfchad.learning_monitoring_system.repository;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smurfchad.learning_monitoring_system.model.Schedule;



@Repository 
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    boolean existsByCourseId(Long courseId);

    boolean existsByGroupId(long groupId);

    Page<Schedule> findAllByGroupId(Long id, Pageable pageable);

    Page<Schedule> findAllByCourseTeacherId(Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Schedule s WHERE s.endTime < :time")
    int deleteAllSchedulesBefore(@Param("time") LocalDateTime time);
}
