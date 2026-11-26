package com.smurfchad.learning_monitoring_system.cleaner;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smurfchad.learning_monitoring_system.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j 
public class Cleaner {

    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldSchedules(){
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        int count = scheduleRepository.deleteAllSchedulesBefore(oneYearAgo);
        log.info("удалено расписаний: " + count);
    }

}
