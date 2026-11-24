package com.smurfchad.learning_monitoring_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "schedules")
@Getter 
@Setter 
@NoArgsConstructor 
public class Schedule {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "group_id", nullable = false)
    private Group group;
    
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "course_id", nullable = false)
    private Course course;

    @Column (name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column (name = "end_time", nullable = false)
    private LocalDateTime endTime;

}
