package com.example.hello.serviceImpl;

import com.example.hello.dao.SearchMapper;
import com.example.hello.entity.Resource;
import com.example.hello.service.DataUpdateService;
import com.example.hello.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataUpdateServiceImpl implements DataUpdateService {
    public static final String bilibiliFile = "C://project/fengyao/slinum/bilibili.py";
    public static final String wyFile = "C://project/fengyao/slinum/wy.py";
    public static final String moocFile = "C://project/fengyao/slinum/mooc.py";

    @Autowired
    private SearchMapper searchMapper;

    @Override
    public int bilibiliDataUpdate(String keyword) {
        System.out.println("bilibiliDataUpdate.....");
        return this.runPython(bilibiliFile,keyword);
    }
    @Override
    public int wyDataUpdate(String keyword) {
        return this.runPython(wyFile,keyword);
    }

    @Override
    public int moocDataUpdate(String keyword) {
        return this.runPython(moocFile,keyword);
    }

    public List<Resource> getResources(String keyword){
        return this.searchMapper.getCourseByAll(keyword);
    }


    public int runPython(String file,String keyword){
        System.out.println("keyword:"+keyword);
        String exe = "python";
        String[] cmdArr = new String[] { exe, file,keyword};
        try {
            Process process = Runtime.getRuntime().exec(cmdArr);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            //输出结果
            System.out.println(in.readLine());
            in.close();
            process.destroy();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
