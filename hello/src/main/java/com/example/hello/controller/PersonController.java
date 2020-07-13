package com.example.hello.controller;

import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.entity.User;
import com.example.hello.service.PersonService;
import com.example.hello.service.RecommendService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/pageJump")
    public String pageJump(@RequestParam("pageName") String pageName){
        return pageName;
    }

    @RequestMapping("/person")
    public String personJump(){return "person";}


    @RequestMapping("/login")
    public String login(@Param("username") String username,
                        @Param("password") String password,
                        @Param("requestUrl") String requestUrl,
                        Map<String,Object> map){
        System.out.println("username : "+username+" password : "+password);
        System.out.println("当前页面的请求"+requestUrl);
        User user = this.personService.getUserByUsernameAndPassword(username, password);
            //登录成功，获取到用户的信息
        map.put("user",user);
        map.put("requestUrl",requestUrl);
//            map.put("resourceList",this.recommendService.getResourceByUserLabel(username));
//            map.put("satisfiedResourceList",this.recommendService.getSatisfiedResourceList());

        return "login";

    }

    @RequestMapping("/exit")
    public String exit(Map<String,Object> map){
        map.put("satisfiedResourceList",this.recommendService.getSatisfiedResourceList());
        return "index";
    }


    @RequestMapping("/register")
    public String register(@Param("username") String username,
                           @Param("password") String password,
                           @Param("email") String email,
                           @Param("phone") String phone,
                           @Param("interest") String interest,
                           Map<String,Object> map){
        System.out.println(username+","+password+","+phone+","+email+","+interest);
        boolean registerStatus = this.personService.register(username, password, email, phone,interest);
        map.put("registerStatus",registerStatus);
        return "register_2";
    }

    @RequestMapping("/password")
    public String password(@RequestParam("username") String username){
        System.out.println(username);
        return "password";
    }

    @RequestMapping("/updatePersonInfo")
    public String updatePersonNamePhoneAndEmail(@Param("name") String name,
                                                @Param("phone") String phone,
                                                @Param("email") String email,
                                                @Param("username") String username,
                                                Map<String,Object> map){
        System.out.println("修改用户信息："+name+","+phone+","+email+","+username);
        map.put("updateInfoStatus",this.personService.updatePersonNamePhoneAndEmail(name, phone, email, username));
        return "person";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(@Param("username") String username,
                                 @Param("prePassword") String prePassword,
                                 @Param("newPassword") String newPassword,
                                  Map<String,Object> map){
        //先判断旧密码是否正确
        System.out.println(username+","+prePassword+","+newPassword);
        if(this.personService.getUserByUsernameAndPassword(username,prePassword) == null){
            //说明账号和旧密码不匹配
            map.put("updatePasswordStatus",false);
        }else{
            //账号密码匹配，则进行修改
            map.put("updatePasswordStatus",this.personService.updatePassword(newPassword, username));
        }
        return "person";
    }

    //显示用户详细信息
    @RequestMapping("/personInfo")
    public String personInfo(@RequestParam("username") String username, Map<String,Object> map){
        System.out.println(username);
        User user = this.personService.personInfo(username);
        System.out.println(user.toString());
        map.put("user",user);
        return "personInfo";
    }


    @RequestMapping("/label")
    public String label(@RequestParam("username") String username,Map<String,Object> map){
        System.out.println(username);
        List<Keyword> labelList = this.personService.getUserLabel(username);
        map.put("labelList",labelList);
        return "individualization";
    }

    @RequestMapping("/updateUserLabel")
    public String updateUserLabel(@Param("username") String username,
                                  @Param("label") String label,
                                  Map<String,Object> map){
        this.personService.updateUserLabel(label,username);
        //再次查询
        List<Keyword> labelList = this.personService.getUserLabel(username);
        map.put("labelList",labelList);
        return "person";
    }

    @RequestMapping("/history")
    public String history(@RequestParam("username") String username,Map<String,Object> map){
        System.out.println("历史记录,"+"username : "+username);
        List<Resource> resourceList = this.personService.getHistory(username);
        map.put("resourceList",resourceList);
        return "video";
    }

    @RequestMapping("/collection")
    public String collection(@RequestParam("username") String username,Map<String,Object> map){
        System.out.println("收藏记录,"+"username : "+username);
        List<Resource> resourceList = this.personService.getCollection(username);
        map.put("resourceList",resourceList);
        return "video";
    }
}
