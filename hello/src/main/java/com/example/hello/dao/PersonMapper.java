package com.example.hello.dao;

import com.example.hello.entity.Resource;
import com.example.hello.entity.User;
import jnr.ffi.annotations.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PersonMapper {
    @Select("select * from person where username=#{username} and password=#{password}")
    User getUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    @Insert("insert into person (username,password,phone,email,label) values(#{username},#{password},#{phone},#{email},#{label})")
    void register(@Param("username") String username,
                  @Param("password") String password,
                  @Param("phone") String phone,
                  @Param("email") String email,
                  @Param("label") String interest);

    @Select("select * from person where username = #{username}")
    User personInfo(@Param("username") String username);


    //查询用户历史记录
    @Select("select history from person where username = #{username}")
    String getHistoryStr(@Param("username") String username);



    //查询用户收藏记录
    @Select("select collection from person where username = #{username}")
    String getCollectionStr(@Param("username") String username);



    //根据视频id查询相应的标签
    @Select("select label from resource where tid in(${tidStr})")
    List<String> getVideoLabel(@Param("tidStr") String tidStr);

    //根据条件(历史、收藏)查询用户资源
    //SELECT * FROM resource WHERE tid IN('1209346809','1005226030','1209346809')
    // ORDER BY INSTR(",'1209346809','1005226030','1209346809',",CONCAT(',',tid,','))
    @Select("SELECT * FROM resource WHERE tid IN(${condition}) ORDER BY INSTR(\",${condition},\",CONCAT(',',tid,','))")
    List<Resource> getResource(@Param("condition") String condition);

    @Select("select * from resource where tid = #{tid} limit 1")
    Resource getOneResource(@Param("tid") String tid);

    //修改用户登录密码
    @Update("update person set password = ${password} where username = ${username}")
    void updatePassword(@Param("password") String password,@Param("username") String username);

    //获取用户标签
    @Select("select label from person where username = #{username}")
    String getUserLabel(@Param("username") String username);

    //更改用户的标签
    @Update("update person set label = #{labelStr} where username = #{username}")
    void updateUserLabel(@Param("labelStr") String labelStr,@Param("username") String username);

    //更新用户信息
    @Update("update person set name = #{name},phone=#{phone},email = #{email} where username = #{username}")
    void updatePersonNamePhoneAndEmail(@Param("name") String name,
                                       @Param("phone") String phone,
                                       @Param("email") String email,
                                       @Param("username") String username);
}
