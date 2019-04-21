package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import java.util.*;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private Map<String,Precinct> precincts = new HashMap<>();
    private Map<String, Cluster> clusters = new HashMap<>();
    private  State s;
    private int desireNum;


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
        this.desireNum=desireDistrict;
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
        s.setPopulation(0);
        for(Precinct p :precincts.values()){
            s.setPopulation((int) (s.getPopulation()+p.getPop100()));
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
                    c1.assignedColor(colors);
                    c2.assignedColor(colors);
                    //clusterEdge.computJoin();//compute joinability of the two precincts
//                    System.out.println(clusterEdge);
                }
            }
        }

    }
    private void phaseone(){
        int v=0;
        State s=new State();
        while(clusters.size()>desireNum) {
            List<String> keysAsArray = new ArrayList<String>(clusters.keySet());
            Random r = new Random();
            Cluster c1=clusters.get(keysAsArray.get(r.nextInt(keysAsArray.size())));
            while(s.getPopulation()/clusters.size()> c1.getPopulation()) {
                double maxjoin=0;
                ClusterEdge desireClusterEdge=null;
                for(ClusterEdge e:c1.getAllEdges()){
                    Cluster c2=e.getNeighborCluster(c1);
                    if(maxjoin<e.getJoinability()){
                        maxjoin=e.getJoinability();
                        desireClusterEdge=e;
                    }

                }
                combine(desireClusterEdge,c1);
            }

        }
    }

    public void combine(ClusterEdge e,Cluster c1){
        Cluster c2 = e.getNeighborCluster(c1);
        c2.removeEdge(e);
        c1.removeEdge(e);
        c1.combineCluster(c2);
        for(ClusterEdge e1: c1.getAllEdges()){
            for(ClusterEdge e2 : c2.getAllEdges()){
                Cluster c3 = e2.getNeighborCluster(c2);
                if(e1.getNeighborCluster(c1)==c3){
                    c3.removeEdge(e2);
                    c2.removeEdge(e2);
                }
            }
            //cut c5
            //add c5 to c1

        }
        //for loop
        //compute all c1 edges

    }

    public void run(){


            init();
        phaseone();
    }
}
