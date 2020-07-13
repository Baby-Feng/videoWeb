package com.example.hello.controller;

import com.example.hello.service.FeedbackService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    //更新用户反馈
    @RequestMapping("/update&feedback")
    public String updateFeedback(@Param("satisfy") int satisfy,
                                 @Param("id") int id){
        System.out.println("satisfy : "+satisfy+" id : "+id);
        this.feedbackService.updateFeedback(satisfy,id);

        return "";

    }


}
