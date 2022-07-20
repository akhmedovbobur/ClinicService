package com.projects.clinicservice.controller;

import com.projects.clinicservice.dto.PatientDto;
import com.projects.clinicservice.model.PatientFilterDto;
import com.projects.clinicservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id){
        PatientDto result = patientService.get(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public  ResponseEntity<?> create(@RequestBody @Valid PatientDto patient){
        PatientDto result = patientService.create(patient);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody @Valid PatientDto patient){
        boolean result = patientService.update(id, patient);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        boolean result = patientService.delete(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam("s") Integer size,
                                    @RequestParam("p")Integer page){
        List<PatientDto> result = patientService.findAllByPage(page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody PatientFilterDto patientFilterDto){
        List<PatientDto> result = patientService.filter(patientFilterDto);
        return ResponseEntity.ok(result);
    }
}
