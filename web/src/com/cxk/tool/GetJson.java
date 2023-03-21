package com.cxk.tool;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
/*
从请求中获取json对象工具类
 */
public class GetJson {

    public static JSONObject getJSON(HttpServletRequest req) {
        StringBuilder str=new StringBuilder();
        try {
            BufferedReader reader = req.getReader();
            String line=reader.readLine();
            while(line!=null){
                str.append(line);
                line=reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回json对象
        return JSONObject.parseObject(str.toString());
    }
}
