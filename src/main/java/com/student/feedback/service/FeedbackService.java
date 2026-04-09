package com.student.feedback.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.feedback.entity.Feedback;
import com.student.feedback.repository.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // SAVE FEEDBACK
    public Feedback saveFeedback(Feedback feedback) {
        feedback.setTimestamp(LocalDateTime.now());
        feedback.setStatus("SUBMITTED");
        return feedbackRepository.save(feedback);
    }

    // GET ALL FEEDBACKS
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
