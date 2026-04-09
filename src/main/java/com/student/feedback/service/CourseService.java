package com.student.feedback.service;

import java.util.List;
import com.student.feedback.entity.Course;

public interface CourseService {

    // ✅ GET ALL COURSES
    List<Course> getAllCourses();

    // ✅ ADD COURSE
    Course addCourse(Course course);

    // ✅ UPDATE COURSE (NEW)
    Course updateCourse(Long id, Course course);

    // ✅ DELETE COURSE
    void deleteCourse(Long id);
}