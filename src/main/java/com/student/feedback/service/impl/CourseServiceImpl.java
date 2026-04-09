package com.student.feedback.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.feedback.entity.Course;
import com.student.feedback.repository.CourseRepository;
import com.student.feedback.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // ✅ GET ALL COURSES
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // ✅ ADD COURSE
    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // ✅ UPDATE COURSE (NEW)
    @Override
    public Course updateCourse(Long id, Course course) {

        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        existing.setName(course.getName());
        existing.setCode(course.getCode());
        existing.setDepartment(course.getDepartment());
        existing.setInstructor(course.getInstructor());

        return courseRepository.save(existing);
    }

    // ✅ DELETE COURSE
    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}