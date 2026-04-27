package com.student.feedback.controller;

import com.student.feedback.entity.ServiceEntity;
import com.student.feedback.repository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    private ServiceRepository repo;

    // ✅ GET ALL
    @GetMapping
    public List<ServiceEntity> getAll() {
        return repo.findAll();
    }

    // ✅ ADD
    @PostMapping
    public ServiceEntity add(@RequestBody ServiceEntity service) {
        return repo.save(service);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
