package com.smurfchad.learning_monitoring_system.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "students")
@Getter 
@Setter 
@NoArgsConstructor 
public class Student {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "first_name", nullable = false)
    private String firstName;

    @Column (name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty 
    @ManyToMany 
    @JoinTable (
        name = "students_groups",
        joinColumns = @JoinColumn (name = "student_id"),
        inverseJoinColumns = @JoinColumn (name = "group_id")
    )
    private Set<Group> groups = new HashSet<>();

    public void addGroup(Group group){
        this.groups.add(group);
        group.getStudents().add(this);
    }

    public void removeGroup(Group group){
        this.groups.remove(group);
        group.getStudents().remove(this);
    }

}
