package com.student.feedback.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feedback") // ✅ FIXED TABLE NAME
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId; // ✅ FIXED (was Long)

    private String studentName;

    private String type; // COURSE / INSTRUCTOR / SERVICE

    private String targetId; // ✅ FIXED (was Long)

    private String targetName;

    private int rating;

    @Column(length = 2000) // ✅ increased size
    private String comments;

    private String category;

    private LocalDateTime timestamp;

    private String status;
}
