package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import javax.validation.constraints.Null;
import java.util.*;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private  State s;

    private int desireNum;
    StringBuilder sb = new StringBuilder();
    String[] colors = {"#8B4513","#8B0000","#006400","#00008B","#FF00FF","#2F4F4F","#FF8C00","#6B5B95","#FFA07A","#00FF7F"};

    private SocketIOClient client;
    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName, int desireDistrict, int numOfRun, PrecinctService precinctService,SocketIOClient client) {
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
        this.s=new State();
        this.desireNum=desireDistrict;
        this.precinctService = precinctService;
        this.client = client;
    }

    private void init(){
        sendMessage("fetching precinct's data...");
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        for(Precinct p : allPrecincts){
            s.addPrecinct(p);
        }
        sendMessage("Construct Clusters...");
        for(Precinct p : s.getPrecincts().values()){
            s.addCluster(new Cluster(p));
        }
        sendMessage("Creating Edge...");
        s.initState();
    }


    private void graphPatition(){
        System.out.println("phaseone");
        sendMessage("Phase 1 Graph partition...");
        while(s.getClusters().size()>desireNum) {
            List<String> keysAsArray = new ArrayList<String>(s.getClusters().keySet());
            Random r = new Random();
            Cluster c1=s.getClusters().get(keysAsArray.get(r.nextInt(keysAsArray.size())));
            System.out.println("1: "+c1.getClusterID()+"  "+s.getClusters().size());
            while(s.getPopulation()/s.getClusters().size()> c1.getDemo().getPopulation() && s.getClusters().size()>desireNum) {
                double maxjoin=0;
                ClusterEdge desireClusterEdge=null;
                System.out.println(" s.getPopulation()/clusters.size()> c1.getPopulation(): "+s.getPopulation()/s.getClusters().size()+", "+c1.getDemo().getPopulation());
                for(ClusterEdge e:c1.getAllEdges()){
                    Cluster c2=e.getNeighborCluster(c1);
                    System.out.println("2: "+c2.getClusterID()+", "+e.getJoinability());
                    if(maxjoin<e.getJoinability()){
                        maxjoin=e.getJoinability();
                        desireClusterEdge=e;
                    }
                }
                if(desireClusterEdge!=null){
                    System.out.println("3: "+desireClusterEdge.getNeighborCluster(c1));
                    combine(desireClusterEdge,c1);}
            }
        }
    }

    private void combine(ClusterEdge e, Cluster c1){
        System.out.println("combine");
        Cluster c2 = e.getNeighborCluster(c1);
        System.out.println(c2.getClusterID());
        sb.append(c2 + " merge into " + c1).append("'\n'");
        c2.removeEdge(e);
        c1.removeEdge(e);
        c2.removeDuplicateEdge(c1);//remove c4
        for(ClusterEdge e2 : c2.getAllEdges()){ //add edges(c5) from c2 to c1
            e2.changeNeighbor(e2.getNeighborCluster(c2),c1);
            c1.addEdge(e2);
        }
        c1.combineCluster(c2);//combine demo data
        s.removeCluster(c2);
        for(ClusterEdge e1 : c1.getAllEdges()){//re-compute c1 join
            e1.computJoin();
        }
    }

    public void run(){
        sendMessage("Algorithm Start...");
            init();
        graphPatition();
        sb.append("'\n'");
       int pn=0;
        int cn=1;
        for(Cluster c:s.getClusters().values()){
            System.out.println(c.getClusterID()+" : precinct size "+c.getPrecincts().size()+", population "+c.getDemo().getPopulation());
            sb.append("No."+cn+": "+c.getClusterID()+" : precinct size "+c.getPrecincts().size()+", population "+c.getDemo().getPopulation()).append("'\n'");
            pn+=c.getPrecincts().size();
            cn++;
        }
        System.out.println("total precinct size: "+pn);


        String temp = "";
        int counter = 0;
        sendMessage("Assign Colors...");
        for(Cluster c : s.getClusters().values()){
            c.setColor(colors[counter]);
            System.out.println("color :"+colors[counter]);
            counter++;

            for(Precinct ps: c.getPrecincts()){
                temp += ps.getId() + ":" + c.getColor() + ",";


            }
            client.sendEvent("updateColor", temp);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            temp="";
        }
        sendMessage(sb.toString());
        sendMessage("Algorithm finished!");
//
    }

    public void sendMessage(String msg){
        client.sendEvent("message", msg);
    }
}
