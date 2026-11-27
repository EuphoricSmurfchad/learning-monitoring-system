package com.smurfchad.learning_monitoring_system.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "courses")
@Getter 
@Setter
@NoArgsConstructor 
public class Course {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name")
    private String name;

    @Column (name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany (mappedBy = "courses")
    private Set<Group> groups = new HashSet<>();

    @OneToMany (mappedBy = "course")
    private List<Schedule> schedules = new ArrayList<>();

    public void addGroup(Group group){
        this.groups.add(group);
        group.getCourses().add(this);
    }

    public void removeGroup(Group group){
        this.groups.remove(group);
        group.getCourses().remove(this);
    }



}
