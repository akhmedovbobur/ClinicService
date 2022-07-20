package com.projects.clinicservice.repository;

import com.projects.clinicservice.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository  extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByIdAndDeletedAtIsNull(Integer id);

    Page<Patient> findAll(Specification<Patient> specifications, Pageable pageable);
}
