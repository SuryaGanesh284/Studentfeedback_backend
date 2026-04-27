package com.student.feedback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.student.feedback.entity.Course;
import com.student.feedback.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 🎓 STUDENT + ADMIN → view courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // 👑 ADMIN ONLY → add course
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course);
    }

    // 🔄 👑 ADMIN ONLY → update course (NEW)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    // ❌ 👑 ADMIN ONLY → delete course
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "Course deleted successfully";
    }
}
