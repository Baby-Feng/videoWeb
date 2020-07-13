package com.example.hello.serviceImpl;

import com.example.hello.dao.FeedbackMapper;
import com.example.hello.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;


    @Override
    public void updateFeedback(int satisfy, int id) {
        if(satisfy==1) this.feedbackMapper.updateYes(id);
        else if(satisfy==0) this.feedbackMapper.updateNo(id);
    }
}
