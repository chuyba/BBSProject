package com.cxk.tool;


import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManger{
    private static ConcurrentHashMap<String, HttpSession> map=new ConcurrentHashMap();
    public static void addSession(HttpSession session){
        if(session!=null){
            map.put(session.getId(),session);
        }
    }

    public static void remove(String sessionId){
        map.remove(sessionId);
    }

    public static HttpSession getSession(String sessionId){
        return map.get(sessionId);
    }

}
