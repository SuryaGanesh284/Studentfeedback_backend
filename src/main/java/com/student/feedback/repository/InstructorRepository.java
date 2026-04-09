package com.student.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.student.feedback.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
