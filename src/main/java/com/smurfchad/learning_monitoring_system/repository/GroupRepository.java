package com.smurfchad.learning_monitoring_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smurfchad.learning_monitoring_system.model.Group;

@Repository 
public interface GroupRepository extends JpaRepository<Group, Long> {

}
