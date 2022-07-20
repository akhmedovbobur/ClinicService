package com.projects.clinicservice.repository;


import com.projects.clinicservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByIdAndDeletedAtIsNull(Integer Id);
}
