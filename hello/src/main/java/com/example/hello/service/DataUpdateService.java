package com.example.hello.service;

import com.example.hello.entity.Resource;

import java.util.List;

public interface DataUpdateService {
    int bilibiliDataUpdate(String keyword);
    int wyDataUpdate(String keyword);
    int moocDataUpdate(String keyword);
    List<Resource> getResources(String keyword);
}
