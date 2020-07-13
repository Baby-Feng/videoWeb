package com.example.hello.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataUpdateMapper {

    @Insert("insert into resource(tid,title,type,author,platform,url) values(#{tid}，#{title},)")
    void bilibiliDataUpdateToMysql(@Param("keyword") String keyword);

}
