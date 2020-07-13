package com.example.hello.controller;

import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.service.SearchService;
import org.apache.ibatis.annotations.Param;
import org.python.icu.text.StringSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    //搜索API
    //网易云课堂搜索：https://study.163.com/courses-search?keyword=springboot
    //bilibili：https://search.bilibili.com/all?keyword=java
    //中国大学生慕课：https://www.icourse163.org/search.htm?search=C#/

    /*
    * 步骤：
    * 1.先从以上三个网站各选取5个最优视频
    *
    *
    *
    * */



    @RequestMapping("/search")
    public String search(@Param("search") String search,  Map<String,Object> map){
        try {
            System.out.println("搜索字符串为："+search);
            List<Resource> resourceList = this.searchService.getResourceList(search);
            System.out.println("资源数"+resourceList.size());
            if(resourceList!=null){
                int resourceSum = resourceList.size();
                int pageSum = (int)Math.ceil(resourceSum/20);
                //总页数
                map.put("pageSum",pageSum);
                map.put("nowPage",1);
                map.put("search",search);
                String search1 = this.searchService.toASCI(search);
                System.out.println("search1："+search1);
                map.put("search1",search1);
                if(resourceSum < 20)map.put("resourceList",resourceList.subList(0,resourceSum));
                else map.put("resourceList",resourceList.subList(0,20));
            }else map.put("resourceList",null);
        }catch (Exception e){
            map.put("resourceList",null);
        }


        return "detail";
    }
    @RequestMapping("/search1")
    public String search1(@RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "searchStr") String searchStr,
                          Map<String,Object> map){
        try {
            System.out.println("分页搜索字符串："+searchStr);
            //解码
            String searchTemp = this.searchService.back(searchStr);
            List<Resource> resourceList = this.searchService.getResourceList(searchTemp);
            int start = (page-1) * 20;
            int end = page * 20;
            int pageSum = (int)Math.ceil(resourceList.size()/20);
            map.put("pageSum",pageSum);
            map.put("nowPage",page);
            map.put("search",searchTemp);
            map.put("search1",searchStr);
            map.put("resourceList",resourceList.subList(start,end));
        }catch (Exception e){
            map.put("resourceList",null);
        }

        return "detail";
    }

    @RequestMapping("/classNameSearch")
    public String classNameSearch(@Param("className") String className,
                                  Map<String,Object> map){
        try {
            System.out.println("分类名称:"+className);
            List<Resource> resourceList = this.searchService.gerResourceByClassName(className);
            if(resourceList!=null){
                //查询到记录
                int resourceSum = resourceList.size();
                int pageSum = (int)Math.ceil(resourceSum/20);
                //总页数
                map.put("pageSum",pageSum);
                map.put("nowPage",1);
                map.put("search",className);
                map.put("search1",this.searchService.toASCI(className));
                if(resourceSum < 20)map.put("resourceList",resourceList.subList(0,resourceSum));
                else map.put("resourceList",resourceList.subList(0,20));

            }else map.put("resourceList",null);
        }catch (Exception e){
            map.put("resourceList",null);
        }

        return "detail";
    }
}
