package com.example.hello.service;

import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonService {
    User getUserByUsernameAndPassword(String username,String password);

    boolean register(String username,String password,String email,String phone,String interest);
    User personInfo(String username);

    boolean updatePersonNamePhoneAndEmail(String name,String phone,String email,String username);
    boolean updatePassword(String password,String username);

    List<Resource> getHistory(String username);
    List<Resource> getCollection(String username);
    List<Resource> getResource(String[] info);
    List<Keyword> getUserLabel(String username);
    void updateUserLabel(String labelStr,String username);

    List<Keyword> getUserLabelListByHistoryAndCollection(String username);
}
