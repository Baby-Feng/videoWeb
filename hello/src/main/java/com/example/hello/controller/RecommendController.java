package com.example.hello.controller;

import com.example.hello.entity.Resource;
import com.example.hello.service.RecommendService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
public class RecommendController {
    //输入用户的历史记录（集合），输出推荐的内容链接
    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/index")
    public String recommendation(@Param("username") String username,Map<String,Object> map){
        //取得推荐结果
        System.out.println("username="+username);
        if(username!=null && username.length()!=0) {
            System.out.println("用户名为空");
            map.put("resourceList",this.recommendService.getResourceByUserLabel(username));
        }
        map.put("satisfiedResourceList",this.recommendService.getSatisfiedResourceList());
        return "index";
    }
}
