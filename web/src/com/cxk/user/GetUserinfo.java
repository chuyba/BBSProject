package com.cxk.user;

import com.alibaba.fastjson.JSONObject;
import com.cxk.tool.GetJson;
import com.cxk.tool.ConnectionManger;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
获取用户信息请求
 */
public class GetUserinfo extends HttpServlet {
    private ServletContext context;

    @Override
    public void init() throws ServletException {
        context=this.getServletContext();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取json信息
        JSONObject json= GetJson.getJSON(req);
        if(!json.containsKey("id")){
            json.remove("id");
            json.put("status",Integer.valueOf(context.getInitParameter("message_err")));
            resp.getWriter().print(json.toString());
        }else{
            int id=json.getInteger("id");
            Connection con=null;
            PreparedStatement statement=null;
            ResultSet result=null;
            try {
                //查询信息返回结果
                con= ConnectionManger.getConnection();
                statement=con.prepareStatement("select id,username,phonenum,email,studentid,realname,sex from userinfo where id=?");
                statement.setInt(1,id);
                result=statement.executeQuery();
                json.remove("id");
                result=statement.executeQuery();
                result.next();
                //将查询信息保存在json对象中并返回
                json.put("status",Integer.valueOf(context.getInitParameter("success")));
                json.put("id",result.getInt("id"));
                json.put("username",result.getString("username"));
                json.put("phonenum",result.getString("phonenum"));
                json.put("email",result.getString("email"));
                json.put("studentid",result.getString("studentid"));
                json.put("realname",result.getString("realname"));
                json.put("sex",result.getInt("sex"));
                resp.getWriter().print(json.toString());
            } catch (SQLException e) {
                resp.getWriter().print(JSONObject.parseObject("{status:2002}"));
                e.printStackTrace();
            }finally {
                ConnectionManger.close(con,statement,result);
            }

        }

    }
}
