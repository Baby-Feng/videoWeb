package com.example.hello;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hello.entity.Resource;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class test1 {
    public static void main(String[] args) throws Exception{
       String a = "12|34";
       System.out.println(a.length());
       System.out.println(a.split("|")[1]);
    }
}