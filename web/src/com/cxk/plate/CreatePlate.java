package com.cxk.plate;

import com.alibaba.fastjson.JSONObject;
import com.cxk.tool.GetJson;
import com.cxk.tool.ConnectionManger;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import java.io.*;

/*
处理创建板块请求
 */
public class CreatePlate extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        JSONObject json= GetJson.getJSON(req);
        String plateName=json.getString("platename");
        String time=json.getString("time");
        Connection con=null;
        PreparedStatement statement=null;
        Cookie cookie[]=req.getCookies();
        String id="";
        int plateMangerId;
        for(Cookie tmp:cookie){
            if(tmp.getName()=="id"){
                id=tmp.getValue();
                break;
            }
        }
        if(id==null){
            resp.getWriter().print("{\"status\":2001}");
            return;
        }
        try{
            con= ConnectionManger.getConnection();
            statement=con.prepareStatement("select id from userinfo where cookie=?");
            statement=con.prepareStatement("insert ignore into plate(plateName,plateMangerId,time) values(?,?,?)");
            statement.setString(1,plateName);

            statement.setString(3,time);
            json=JSONObject.parseObject("{}");
            if(statement.executeUpdate()==0){
                json.put("status",context.getInitParameter("fail"));
            }else{
                json.put("status",context.getInitParameter("success"));
            }
            resp.getWriter().print(json.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            ConnectionManger.close(con,statement);
        }
    }
}
