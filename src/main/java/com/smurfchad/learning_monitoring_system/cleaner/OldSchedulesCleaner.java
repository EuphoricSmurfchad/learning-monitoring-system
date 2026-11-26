package com.smurfchad.learning_monitoring_system.cleaner;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smurfchad.learning_monitoring_system.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OldSchedulesCleaner {

    private final ScheduleService scheduleService;

    @Scheduled(cron = "${app.cleanup.app}")
    public void cleanOldSchedules() {
        int count = scheduleService.cleanOldSchedules();
        log.info("удалено расписаний: " + count);
    }

}
