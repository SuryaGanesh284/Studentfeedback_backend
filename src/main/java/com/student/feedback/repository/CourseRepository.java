package com.student.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.feedback.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}