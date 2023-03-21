package com.cxk.user;

import com.alibaba.fastjson.JSONObject;
import com.cxk.tool.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

/*
处理登录请求
 */
public class Login extends HttpServlet {
    private ServletContext context;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取json与servletcontext对象
        context=this.getServletContext();
        JSONObject json=GetJson.getJSON(req);
        if(json==null||json.size()!=3){
            resp.getWriter().print("{\"status:\"2001}");
        }else{
            //获取登录方式
            int method=json.getInteger("logintype");
            String sql="";
            Connection con= null;
            PreparedStatement statement=null;
            ResultSet result=null;
            try {
                con= ConnectionManger.getConnection();
                String filed="";
                //根据登录方式填写字段
                if(method==1){
                    filed="phonenum";
                }else if(method==2){
                    filed="email";
                }else{
                    filed="username";
                }
                //获取数据
                statement=con.prepareStatement("select count(*) as count,id,username from userinfo where "+filed +"=? and password=?");
                String username=json.getString("username");
                String password=json.getString("password");
                statement.setString(1,username);
                statement.setString(2,password);
                result=statement.executeQuery();
                result.next();
                json=JSONObject.parseObject("{}");
                //判断登录是否成功
                if(result.getInt("count")==0){//登录失败
                    json.put("status",Integer.valueOf(context.getInitParameter("fail")));
                    json.put("username","");
                    resp.getWriter().print(json.toString());
                }else{
                    //登录成功
                    int id=result.getInt("id");
                    HttpSession session=req.getSession();
                    session.setAttribute("username",result.getString("username"));
                    session.setAttribute("password",password);
                    session.setAttribute("id",id);
                    SessionManger.addSession(session);
                    String cookie=session.getId();
                    cookie+=BCrypt.hashpw(cookie,BCrypt.gensalt());
                    resp.addCookie(new Cookie("id",cookie));
                    json.put("status",context.getInitParameter("success"));
                    json.put("id",id);
                    json.put("username",result.getString("username"));


                }
            }catch (SQLException e) {
                e.printStackTrace();
                json.put("status",Integer.getInteger(context.getInitParameter("server_err")));
                json.put("username","");
                resp.getWriter().print(json.toString());
            } finally{
                ConnectionManger.close(con,statement,result);
            }

        }

    }
}
