package com.example.hello.service;


import com.example.hello.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RecommendService {
    //得到推荐的资源目录
    List<Resource> getRecommendation(String tidList,int username);


    List<Resource> getResourceByUserLabel(String username);
    List<Resource> getSatisfiedResourceList();
}
