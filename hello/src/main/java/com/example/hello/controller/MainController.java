package com.example.hello.controller;

import com.example.hello.entity.Resource;
import com.example.hello.service.MainService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MainService mainService;


    @RequestMapping("/main")
    public String main(@RequestParam("tid") String tid,
                           @RequestParam("url") String url,
                           @RequestParam("username") String username,
                           Map<String,Object> map){
        System.out.println("main请求");
        System.out.println(" tid = "+tid +" username = "+username+" url = "+url);
        //添加该次点击视频到历史记录,并返回该视频链接
        if(username!=null){
            //前端获取到用户登录账号
            this.mainService.addHistory(tid,username);
            map.put("isResourceInCollection",this.mainService.isResourceInCollection(tid,username));
        }else map.put("isResourceInCollection",-1);
        //该资源播放次数自增
        this.mainService.updateResourcePlay(tid);
        map.put("url",url);
        map.put("tid",tid);
        return "main";
    }

    @RequestMapping("/addCollection")
    public String addCollection(@Param("tid") String tid,
                                @Param("username") String username,
                                @Param("url") String url,
                                Map<String,Object> map){
        System.out.println(" tid = "+tid +" username = "+username);
        if(username!=null){
            int status =  this.mainService.isResourceInCollection(tid,username);
            if(status==1){
                //已经有该收藏记录，则直接返回
                map.put("isResourceInCollection",1);
            }else {
                boolean isResourceInCollection = this.mainService.addCollection(tid, username);
                if(isResourceInCollection) System.out.println("添加收藏成功!");
                else System.out.println("添加收藏失败!");
                map.put("isResourceInCollection",this.mainService.isResourceInCollection(tid,username));
            }
        }else map.put("isResourceInCollection",-1);
        //添加到收藏目录中,说明当前资源还没有收藏

        this.mainService.updateResourceCollection(tid);
        //该资源的收藏次数自增1
        map.put("url",url);
        map.put("tid",tid);

        return "main";
        //返回空白页
    }

    @RequestMapping("/deleteCollection")
    public String deleteCollection(@Param("tid") String tid,
                                   @Param("username") String username,
                                   @Param("url") String url,
                                   Map<String,Object> map){
        System.out.println(" tid = "+tid +" username = "+username);
        if(username!=null) {
            if (this.mainService.isResourceInCollection(tid, username) == 0) {
                //没有则直接返回
                map.put("isResourceInCollection", 0);
            } else {
                this.mainService.reduceCollection(tid, username);
                map.put("isResourceInCollection", this.mainService.isResourceInCollection(tid, username));
            }
        }
        map.put("url",url);
        map.put("tid",tid);
        this.mainService.reduceResourceCollection(tid);
        return "main";
    }

}
