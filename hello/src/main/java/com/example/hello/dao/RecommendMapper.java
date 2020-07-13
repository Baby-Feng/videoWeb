package com.example.hello.dao;

import com.example.hello.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecommendMapper {
    @Select("SELECT * FROM resource where title like ${label} ${condition} LIMIT 1")
    Resource getRecommendation(@Param("label") String label,@Param("condition") String condition);

    @Select("select history from person where username = #{username}")
    String getHistoryByUsername(@Param("username") int username);

    @Select("SELECT DISTINCT label from resource where tid in (${tid})")
    String [] getLabelByVideoId(@Param("tid") String tid);
}
