package com.projects.clinicservice.repository;

import com.projects.clinicservice.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit,Integer> {
}
