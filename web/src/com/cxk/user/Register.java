package com.cxk.user;

import com.alibaba.fastjson.JSONObject;
import com.cxk.tool.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
;
import java.sql.*;

/*
处理注册请求
 */
public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取所有注册信息
        JSONObject json = GetJson.getJSON(req);
        if(json==null||json.size()!=8){
            resp.getWriter().print("{\"status\":2001}");
        }else{
            String username=json.getString("username");
            String  email=json.getString("email");
            String phonenum=json.getString("phonenum");
            String password=json.getString("password");
            String stuid=json.getString("studentid");
            String name=json.getString("realname");
            int sex=json.getInteger("sex");
            String introduction=json.getString("introduction");
            Connection con = null;
            PreparedStatement statement =null;
            try {
                //设置sql语句与查询参数
                con= ConnectionManger.getConnection();
                statement=con.prepareStatement("insert ignore into userinfo(username,email,phone,password,studentId,realName,sex,introduction) values (?,?,?,?,?,?,?,?)");
                statement.setString(1,username);
                statement.setString(2,email);
                statement.setString(3,phonenum);
                statement.setString(4,password);
                statement.setString(5,stuid);
                statement.setString(6,name);
                statement.setInt(7,sex);
                statement.setString(8,introduction);
                //根据返回影响行数判断信息是否重复
                if(statement.executeUpdate()==1){
                    resp.getWriter().print("{\"status\":2000}");
                }else{
                    resp.getWriter().print("{\"status\":2003}");
                }
            }catch (SQLException e){
                e.printStackTrace();
                resp.getWriter().print("{\"status\":2002}");

            }  finally {
                ConnectionManger.close(con,statement);
            }
        }


    }
}
