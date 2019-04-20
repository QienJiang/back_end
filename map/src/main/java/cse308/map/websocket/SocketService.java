package cse308.map.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import cse308.map.algorithm.Algorithm;
import cse308.map.server.PrecinctService;
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
    private static String[] colors = {"red","purple","yellow","orange","blue"};
    private static Random r = new Random();
    @Autowired
    private SocketIOServer server;
    @Autowired
    private PrecinctService precinctService;

    private static Map<String, SocketIOClient> clientsMap = new HashMap<String, SocketIOClient>();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientsMap.put(uuid, client);
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " 设备建立连接");
    }


    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientsMap.remove(uuid);
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " 设备断开连接");
    }

    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        System.out.println("发来消息：" + data);
        Algorithm algorithm = new Algorithm("pa",10,1,precinctService,client);
        algorithm.run();

        System.out.println("finished");
        //服务器端向该客户端发送消息
        //socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "你好 data");

        /*
        for(int i = 0; i < 100;i++) {
            client.sendEvent("messageevent", states[r.nextInt(3)] + ":" + colors[r.nextInt(5)]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
    }

    public void sendMessageToAllClient(String eventType, String message) {
        Collection<SocketIOClient> clients = server.getAllClients();
        for (SocketIOClient client : clients) {
            client.sendEvent(eventType, message);
        }
    }
}
