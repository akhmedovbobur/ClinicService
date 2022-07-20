package com.projects.clinicservice.service;

import com.projects.clinicservice.dto.PatientDto;
import com.projects.clinicservice.exception.BadRequest;
import com.projects.clinicservice.model.Patient;
import com.projects.clinicservice.model.PatientFilterDto;
import com.projects.clinicservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    public PatientDto get(Integer id) {
        Patient patient = getEntity(id);
        PatientDto patientDto = new PatientDto();
        convertEntityToDto(patient, patientDto);
        return patientDto;
    }

    public PatientDto create(PatientDto patientDto) {
        Patient patient = new Patient();
        convertDtoToEntity(patientDto, patient);
        patient.setStatus(true);
        patient.setCreatedAt(LocalDateTime.now());
        patientRepository.save(patient);
        patientDto.setId(patient.getId());
        return patientDto;
    }

    public boolean update(Integer id, PatientDto patientDto) {
        Patient update = getEntity(id);
        convertDtoToEntity(patientDto, update);
        update.setUpdatedAt(LocalDateTime.now());
        patientRepository.save(update);
        return true;
    }

    public boolean delete(Integer id) {
        Patient patient = getEntity(id);
        patient.setDeletedAt(LocalDateTime.now());
        patientRepository.save(patient);
        return true;
    }

    public Patient getEntity(Integer id) {
        Optional<Patient> optional = patientRepository.findByIdAndDeletedAtIsNull(id);
        if (optional.isEmpty()) {
            throw new BadRequest("Patient not found");
        }
        return optional.get();
    }

    public void convertDtoToEntity(PatientDto dto, Patient entity) {
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setBirthday(dto.getBirthday());
        entity.setAge(LocalDate.now().getYear() - dto.getBirthday().getYear());
        entity.setContact(dto.getContact());
    }

    public void convertEntityToDto(Patient entity, PatientDto dto) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setAge(entity.getAge());
        dto.setBirthday(entity.getBirthday());
        dto.setContact(entity.getContact());
    }

    public List<PatientDto> findAllByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> resultPage = patientRepository.findAll(pageable);
        List<PatientDto> response = new LinkedList<>();
        for (Patient patient : resultPage) {
            if (patient.getDeletedAt() == null) {
                PatientDto dto = new PatientDto();
                convertEntityToDto(patient, dto);
                response.add(dto);
            }
        }
        return response;
    }


    public List<PatientDto> filter(PatientFilterDto dto) {
        String sortBy = dto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdAt";
        }
        if (dto.getDirection() == null){
            dto.setDirection(Sort.Direction.ASC);
        }
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), sortBy);
        List<Predicate> predicateList = new LinkedList<>();
        Specification<Patient> specifications = (((root, query, criteriaBuilder) -> {
            if (dto.getName() != null) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%")); // Sa -> Sardor, Sarvar, Samandar, Sanjar
            }
            if (dto.getSurname() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("surname"), dto.getSurname()));
            }
            if (dto.getStartBirth() != null && dto.getEndBirth() != null) {
                predicateList.add(criteriaBuilder.between(root.get("birthday"), dto.getStartBirth(), dto.getEndBirth()));
            }
            if (dto.getContact() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("contact"), dto.getContact()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0])); // findAllBy ---------
        }));
        List<PatientDto> resultList = new LinkedList<>();
        Page<Patient> patientList = patientRepository.findAll(specifications,pageable);
        for (Patient patient : patientList) {
            if (patient.getDeletedAt() == null){
                PatientDto patientDto = new PatientDto();
                convertEntityToDto(patient, patientDto);
                resultList.add(patientDto);
            }
        }
        return resultList;
    }
}
