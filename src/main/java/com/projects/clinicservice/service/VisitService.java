package com.projects.clinicservice.service;

import com.projects.clinicservice.dto.VisitDto;
import com.projects.clinicservice.exception.BadRequest;
import com.projects.clinicservice.model.Visit;
import com.projects.clinicservice.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;

    public VisitDto get(Integer id) {
        Visit visit = getEntity(id);
        VisitDto visitDto = new VisitDto();
        visitDto.setPatient(patientService.get(visit.getPatientId()));
        visitDto.setDoctor(doctorService.get(visit.getDoctorId()));
        visitDto.setDiagnosis(visit.getDiagnosis());
        return visitDto;
    }

    public VisitDto create(VisitDto dto) {
        Visit visit = new Visit();
        //TODO: check doctor
        doctorService.check(dto.getDoctorId());
        visit.setDoctorId(dto.getDoctorId());
        //TODO: check patient
        patientService.getEntity(dto.getPatientId());
        visit.setPatientId(dto.getPatientId());

        visit.setDiagnosis(dto.getDiagnosis());
        visit.setCreatedAt(LocalDateTime.now());
        visit.setStatus(true);
        visitRepository.save(visit);
        return dto;
    }

    public boolean update(Integer id, VisitDto dto) {
        Visit visit = getEntity(id);
        //TODO: check doctor
        doctorService.check(dto.getDoctorId());
        visit.setDoctorId(dto.getDoctorId());
        //TODO: check patient
        patientService.getEntity(dto.getPatientId());
        visit.setPatientId(dto.getPatientId());

        visit.setDiagnosis(dto.getDiagnosis());
        visit.setUpdatedAt(LocalDateTime.now());
        visitRepository.save(visit);
        return true;
    }
    public boolean delete(Integer id) {
        Visit visit = getEntity(id);
        visit.setDeletedAt(LocalDateTime.now());
        visitRepository.save(visit);
        return true;
    }

    public Visit getEntity(Integer id) {
        Optional<Visit> optional = visitRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequest("Visit not found");
        }
        return optional.get();
    }
}
