package com.student.feedback.controller;

import com.student.feedback.entity.Feedback;
import com.student.feedback.service.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // ✅ SAVE FEEDBACK
    @PostMapping
    public Feedback submitFeedback(@RequestBody Feedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    // ✅ GET ALL FEEDBACKS
    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }
}
