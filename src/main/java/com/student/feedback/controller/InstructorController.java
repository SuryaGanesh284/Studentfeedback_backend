package com.student.feedback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.student.feedback.entity.Instructor;
import com.student.feedback.repository.InstructorRepository;

@RestController
@RequestMapping("/api/instructor")
@CrossOrigin("*")
public class InstructorController {

    @Autowired
    private InstructorRepository repo;

    // ✅ GET ALL
    @GetMapping
    public List<Instructor> getAll() {
        return repo.findAll();
    }

    // ✅ ADD
    @PostMapping
    public Instructor add(@RequestBody Instructor instructor) {
        return repo.save(instructor);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
