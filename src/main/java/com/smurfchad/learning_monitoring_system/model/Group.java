package com.smurfchad.learning_monitoring_system.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "groups")
@Getter 
@Setter 
@NoArgsConstructor 
public class Group {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (name = "name", nullable = false)
    private String name;

    @ManyToMany (mappedBy = "groups")
    private Set<Student> students = new HashSet<>();

    @ManyToMany 
    @JoinTable (
        name = "group_courses",
        joinColumns = @JoinColumn (name = "group_id"),
        inverseJoinColumns = @JoinColumn (name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    @OneToMany (mappedBy = "group")
    private List<Schedule> schedules = new ArrayList<>();

    public void addStudent(Student student){
        this.students.add(student);
        student.getGroups().add(this);
    }

    public void removeStudent(Student student){
        this.students.remove(student);
        student.getGroups().remove(this);
    }



}
