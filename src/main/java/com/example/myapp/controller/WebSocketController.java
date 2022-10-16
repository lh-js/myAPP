package com.example.myapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArraySet;

// Server 类
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocketController {

    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet =
            new CopyOnWriteArraySet();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收userId
    private String userId = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        System.out.println("链接成功" + session);
        log.info("userId" + userId);
        this.userId = userId;
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:"+userId+",当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(session);
        System.out.println("链接关闭" + session);
        reduceOnlineCount();
        webSocketSet.remove(this);
        System.out.println(this.userId);
    }

    @OnMessage
    public void onMessage(Session session, String text) throws IOException {
        System.out.println("收到消息：" + text);
        System.out.println(session);
        sendInfo("大家好",userId);
    }

    /**
     * 实现服务器主动推送-字符串
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息-发送文本文件
     */
    public static void sendInfo(String message,@PathParam("userId") String userId){
        log.info("推送消息到窗口"+userId+"，推送内容:"+message);
        for (WebSocketController item : webSocketSet) {
            System.out.println(item.userId);
            try {
                //这里可以设定只推送给这个userId的，为null则全部推送
                if(userId==null) {
                    item.sendMessage(message);
                }else if(item.userId.equals(userId)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketController.onlineCount++;
    }

    public static synchronized void reduceOnlineCount(){
        WebSocketController.onlineCount--;
    }


}
