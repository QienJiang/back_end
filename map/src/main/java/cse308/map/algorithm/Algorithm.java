package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private Map<String,Precinct> precincts = new HashMap<>();
    private Map<String, Cluster> clusters = new HashMap<>();

    private SocketIOClient client;
    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName, int desireDistrict, int numOfRun, PrecinctService precinctService, SocketIOClient client){
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
        this.precinctService = precinctService;
        this.client = client;
    }

    public Algorithm(String stateName, int desireDistrict, int numOfRun, PrecinctService precinctService) {
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
        this.precinctService = precinctService;
    }

    private void init(){
        System.out.print("in");
        String[] colors = {"#FF0000","#00FF00","#0000FF","#FF00FF","#00FF00","blue","#FFFF00"};
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        String temp = "";
        int counter = 0;
        for(Precinct p : allPrecincts){
            precincts.put(p.getId(),p);
//
//            if(counter<100) {
//                temp += p.getId() + ":" + colors[(int) (Math.random() * colors.length)] + ",";
//                counter++;
//            }
//            else{
//                client.sendEvent("messageevent", temp);
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                temp="";
//                counter=0;
//            }
        }

        for(Precinct p : precincts.values()){
            clusters.put(p.getId(),new Cluster(p));
        }

        for(Precinct p :precincts.values()){
            Demographic demo1=new Demographic();
            demo1.setNATIVAAMERICAN(p.getNativeamericanpop());
            demo1.setMajorMinor(MajorMinor.NATIVEAMERICAN);
            p.setDemo(demo1);
            String[] neighbors =  p.getNeighbors().split(",");

            for(String name: neighbors){
                Precinct neighbor = precincts.get(name);
                if(!p.isNeighbor(neighbor)) {
                    Demographic demo2=new Demographic();
                    demo2.setNATIVAAMERICAN(neighbor.getNativeamericanpop());
                    demo2.setMajorMinor(MajorMinor.NATIVEAMERICAN);
                    neighbor.setDemo(demo2);

                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbor);
                    precinctEdge.computJoin();
                    p.addEdge(precinctEdge);
                    neighbor.addEdge(precinctEdge);
                    //precinctEdge.computJoin();//compute joinability of the two precincts

                    Cluster c1 = clusters.get(p.getId());
                    c1.setCountyID(p.getCountyfp10());
                    Cluster c2 = clusters.get(neighbor.getId());
                    c2.setCountyID(neighbor.getCountyfp10());
                    ClusterEdge clusterEdge = new ClusterEdge(c1,c2);
                    clusterEdge.setJoinability(precinctEdge.getJoinability());
                    
                    c1.addEdge(clusterEdge);
                    c2.addEdge(clusterEdge);
                    //clusterEdge.computJoin();//compute joinability of the two precincts
//                    System.out.println(clusterEdge);
                }
            }
        }

    }
    private void combine(){
//        for(Cluster c : clusters.values()){
//            System.out.println(c.getClusterID());
//        }
        Cluster c=clusters.get("42083360");
        Set<ClusterEdge> edges=c.getAllEdges();

        for(ClusterEdge e:edges){
            System.out.println(e);
        }
    }

    public void run(){
            init();
            combine();
    }
}
