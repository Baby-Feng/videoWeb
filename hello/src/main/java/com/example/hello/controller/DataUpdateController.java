package com.example.hello.controller;
import com.example.hello.entity.Resource;
import com.example.hello.service.DataUpdateService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DataUpdateController {
    @Autowired
    private DataUpdateService dataUpdateService;

    //根据关键词进行数据更新
    //调用python程序爬取数据.....
    @RequestMapping("/bilibiliDataUpdate")
    public String bilibiliDataUpdate(@Param("keyword") String keyword, Map<String,Object> map){
        if(this.dataUpdateService.bilibiliDataUpdate(keyword) == 1){
            //更新成功
            System.out.println("ok");
            List<Resource> resourceList = this.dataUpdateService.getResources("%"+keyword+"%");
            System.out.println(resourceList.size());
            map.put("resourceList",resourceList);
            return "person";
        }else{
            return "error";
        }

    }
}
