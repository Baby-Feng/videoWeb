package com.example.hello;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args){
        List<String> test = getPreDivisionStrList("黑[马 程123456序$$员#世界java帅哥是谁");
        for(int i=0;i<test.size();++i){
            System.out.println(test.get(i));
        }
    }
    public static boolean isChinese(char temp){
        if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(
                String.valueOf(temp)).find())
            return false;
        else return true;
    }

    public static boolean isEnglish(char temp){
        if((temp >= 'a' && temp <= 'z') || (temp >= 'A' && temp <= 'Z'))
            return true;
        else return false;
    }

    public static boolean isNumber(char temp){
        Pattern pattern = Pattern.compile("[0-9]");
        return pattern.matcher(String.valueOf(temp)).find();
    }

    public static boolean isSpecialChar(char temp){
        //特殊字符
        Pattern pattern = Pattern.compile("[@#$%&*_+-]");
        return pattern.matcher(String.valueOf(temp)).find();
    }

    public static List<String> getPreDivisionStrList(String search){
        //对搜索字符串进行预分割
        List<String> preDivisionStrList = new ArrayList<>();
        int preTemp = 0;
        int preCharType = 0;
        boolean breakStatus = false;
        //设定分割状态
        for(int i=0;i<search.length();++i){
            char nowChar = search.charAt(i);
            System.out.print(nowChar);
            if(isChinese(nowChar)){
                //是中文
                System.out.println(1);
                if(preCharType != 1){
                    //前一个字符不是中文
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 1;

            }else if(isEnglish(nowChar)){
                //是英文
                System.out.println(2);
                if(preCharType != 2){
                    //前一个字符不是英文
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 2;
            }else if(isNumber(nowChar)){
                //是数字
                System.out.println(3);
                if(preCharType != 3){
                    //前一个字符不是中文
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 3;
            }else if(isSpecialChar(nowChar)){
                //是特殊字符
                System.out.println(4);
                if(preCharType != 4){
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 4;
            }else {
                //其它字符一律作为分割符号
                System.out.println("其它");
                if(preTemp < i)
                    //说明已保存至少一个字符
                    preDivisionStrList.add(search.substring(preTemp,i));
                preTemp = i+1;
                //前驱指针指向后一个字符

            }
        }
        if(preTemp < search.length())
            preDivisionStrList.add(search.substring(preTemp));
        return preDivisionStrList;
    }
}
