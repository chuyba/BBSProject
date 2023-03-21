package com.cxk.tool;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.commons.dbcp2.*;
import java.sql.*;

//连接管理类
public class ConnectionManger extends HttpServlet {
    private static BasicDataSource connectionPool=new BasicDataSource();
    @Override
    //初始化连接池
    public void init()  {
        connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connectionPool.setUsername("root");
        connectionPool.setPassword("5869815han");
        connectionPool.setUrl("jdbc:mysql://localhost:3306/bbs");
        connectionPool.setInitialSize(20);
    }
    //获取连接
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
    //关闭连接
    public static void close(Connection con,PreparedStatement statement,ResultSet result){
        try{
            result.close();
            statement.close();
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void close(Connection con,PreparedStatement statement){
        try{

            statement.close();
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
