package com.smurfchad.learning_monitoring_system.model;



import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "teachers")
@Getter 
@Setter 
@NoArgsConstructor 
public class Teacher {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "first_name", nullable = false)
    private String firstName;

    @Column (name = "last_name", nullable = false)
    private String lastName;

    @OneToMany (mappedBy = "teacher")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany (mappedBy = "teacher")
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course){
        this.courses.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course){
        this.courses.remove(course);
        course.setTeacher(null);
    }

}
