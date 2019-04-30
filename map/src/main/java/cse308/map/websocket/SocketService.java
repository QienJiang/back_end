package cse308.map.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import cse308.map.algorithm.Algorithm;
import cse308.map.model.Configuration;
import cse308.map.server.PrecinctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketService.class);
    private static String[] states = {"New York", "California", "Pennsylvania"};
    private static String[] colors = {"red", "purple", "yellow", "orange", "blue"};
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
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " connection success");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientsMap.remove(uuid);
        LOGGER.debug("IP: " + client.getRemoteAddress().toString() + " UUID: " + uuid + " disconnect from service");
    }

    @OnEvent(value = "runAlgorithm")
    public void onEvent(SocketIOClient client, AckRequest request, @RequestBody Configuration data) {
        System.out.println(data.getNumOfRun());
        Algorithm algorithm = new Algorithm("pa", new Configuration(10, 1,0.5,"NATIVEAMERICAN",0.2,0.2,0.2,0.2,0.2), precinctService, client);
        if(data.getNumOfRun() == 1) {
            algorithm.run();
        }else{
            algorithm.batchRun();
        }
        System.out.println("finished");
    }

    public void sendMessageToAllClient(String eventType, String message) {
        Collection<SocketIOClient> clients = server.getAllClients();
        for (SocketIOClient client : clients) {
            client.sendEvent(eventType, message);
        }
    }
}
