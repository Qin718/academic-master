package com.example.common.webSocket;

import com.example.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/WebSocket/{Id}")  // 接口路径 ws://localhost:8080/webSocket/id;
public class WebSocket {

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static final CopyOnWriteArraySet<WebSocket> WEB_SOCKETS = new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 用户ID
     */
    private String Id;

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "Id") String Id) {
        try {
            if (!sessionPool.containsKey(Id)) {
                this.session = session;
                this.Id = Id;
                WEB_SOCKETS.add(this);
                sessionPool.put(Id, session);
                log.info("【websocket消息】有新的连接,连接者ID:{},总数为:{}", Id, WEB_SOCKETS.size());
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            log.info("用户{}退出", this.Id);
            WEB_SOCKETS.remove(this);
            sessionPool.remove(this.Id);
            log.info("【websocket消息】连接断开，总数为:{}", WEB_SOCKETS.size());
        } catch (Exception ignored) {
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端消息:{}", message);
    }

    /**
     * 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误,原因:{}", error.getMessage());
    }


    // 此为广播消息
    public void sendAllMessage(SendSocket socket) {
        log.info("【websocket消息】广播消息:{}", socket);
        for (WebSocket webSocket : WEB_SOCKETS) {
            try {
                if (webSocket.session.isOpen()) {
                    String message = StringUtils.tojsonstring(socket);
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Exception:" + e);
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String Id, SendSocket socket) {
        Session session = sessionPool.get(Id);
        if (session != null && session.isOpen()) {
            try {
                String message = StringUtils.tojsonstring(socket);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Exception:" + e);
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] Ids, String message) {
        for (String Id : Ids) {
            Session session = sessionPool.get(Id);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:{}", message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
//                    e.printStackTrace();
                    System.out.println("Exception:" + e);
                }
            }
        }

    }

}
