package cse308.map.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SocketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketService.class);
    private static String[] states = {"New York","California","Pennsylvania"};
    private static String[] colors = {"red","purple","yellow"};
    private static Random r = new Random();
    @Autowired
    private SocketIOServer server;

    private static Map<String, SocketIOClient> clientsMap = new HashMap<String, SocketIOClient>();

    /**
     * 添加connect事件，当客户端发起连接时调用，本文中将clientid与sessionid存入数据库
     * //方便后面发送消息时查找到对应的目标client,
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientsMap.put(uuid, client);
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " 设备建立连接");
    }

    /**
     * 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientsMap.remove(uuid);
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " 设备断开连接");
    }

    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        System.out.println("发来消息：" + data);

        //服务器端向该客户端发送消息
        //socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "你好 data");

        for(int i = 0; i < 100;i++) {
            client.sendEvent("messageevent", states[r.nextInt(3)] + ":" + colors[r.nextInt(3)]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给所有连接客户端推送消息
     *
     * @param eventType 推送的事件类型
     * @param message   推送的内容
     */
    public void sendMessageToAllClient(String eventType, String message) {
        Collection<SocketIOClient> clients = server.getAllClients();
        for (SocketIOClient client : clients) {
            client.sendEvent(eventType, message);
        }
    }
}
