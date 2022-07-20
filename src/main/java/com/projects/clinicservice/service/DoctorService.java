package com.projects.clinicservice.service;

import com.projects.clinicservice.dto.DoctorDto;
import com.projects.clinicservice.exception.BadRequest;
import com.projects.clinicservice.model.Doctor;
import com.projects.clinicservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorDto get(Integer id) {
        Doctor doctor = check(id);
        DoctorDto dto = new DoctorDto();
        dto.setName(doctor.getName());
        dto.setSurname(doctor.getSurname());
        dto.setContact(doctor.getContact());
        dto.setDirection(doctor.getDirection());
        dto.setExperience(doctor.getExperience());
        return dto;
    }

    public DoctorDto create(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSurname(dto.getSurname());
        doctor.setContact(dto.getContact());
        doctor.setDirection(dto.getDirection());
        doctor.setExperience(dto.getExperience());
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setStatus(true);
        doctorRepository.save(doctor);
        return dto;
    }


    public boolean update(Integer id, DoctorDto doctor) {
        Doctor update = check(id);
        update.setName(doctor.getName());
        update.setSurname(doctor.getSurname());
        update.setContact(doctor.getContact());
        update.setDirection(doctor.getDirection());
        update.setExperience(doctor.getExperience());
        update.setUpdatedAt(LocalDateTime.now());
        doctorRepository.save(update);
        return true;
    }

    public boolean delete(Integer id) {
        Doctor doctor = check(id);
        doctor.setDeletedAt(LocalDateTime.now());
        doctorRepository.save(doctor);
        return true;
    }

    public Doctor check(Integer id){
        Optional<Doctor> optional = doctorRepository.findByIdAndDeletedAtIsNull(id);
        if (optional.isEmpty()){
            throw new BadRequest("Doctor not found");
        }
        return optional.get();
    }

}
