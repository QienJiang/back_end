package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import javax.validation.constraints.Null;
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
        this.s=new State();
        this.desireNum=desireDistrict;
        this.precinctService = precinctService;
    }

    private void init(){

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
            demo1.setPopulation((int) p.getPop100());
            demo1.setMajorMinor(MajorMinor.NATIVEAMERICAN);
            p.setDemo(demo1);
            String[] neighbors =  p.getNeighbors().split(",");

            for(String name: neighbors){
                Precinct neighbor = precincts.get(name);
                if(!p.isNeighbor(neighbor)) {
                    Demographic demo2=new Demographic();
                    demo2.setNATIVAAMERICAN(neighbor.getNativeamericanpop());
                    demo2.setPopulation((int) neighbor.getPop100());
                    demo2.setMajorMinor(MajorMinor.NATIVEAMERICAN);
                    neighbor.setDemo(demo2);

                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbor);
                    precinctEdge.computJoin();
                    p.addEdge(precinctEdge);
                    neighbor.addEdge(precinctEdge);
                    //precinctEdge.computJoin();//compute joinability of the two precincts

                    Cluster c1 = clusters.get(p.getId());
                    c1.setCountyID(p.getCountyfp10());
                    c1.setDemo(p.getDemo());
                    Cluster c2 = clusters.get(neighbor.getId());
                    c2.setCountyID(neighbor.getCountyfp10());
                    c2.setDemo(neighbor.getDemo());
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
        System.out.println("phaseone");
        int v=0;

        while(clusters.size()>desireNum) {
            List<String> keysAsArray = new ArrayList<String>(clusters.keySet());
            Random r = new Random();
            Cluster c1=clusters.get(keysAsArray.get(r.nextInt(keysAsArray.size())));
            System.out.println("1: "+c1.getClusterID()+"  "+clusters.size());
            while(s.getPopulation()/clusters.size()> c1.getDemo().getPopulation()) {
                double maxjoin=0;
                ClusterEdge desireClusterEdge=null;
                System.out.println(" s.getPopulation()/clusters.size()> c1.getPopulation(): "+s.getPopulation()/clusters.size()+", "+c1.getDemo().getPopulation());
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
        int i=0;
        for(Cluster c:clusters.values()){
            System.out.println(c.getClusterID()+" : precinct size "+c.getPrecincts().size()+", population "+c.getDemo().getPopulation());
            i+=c.getPrecincts().size();
        }
        System.out.println("total precinct size: "+i);
    }

    private void combine(ClusterEdge e, Cluster c1){
        System.out.println("combine");
        Cluster c2 = e.getNeighborCluster(c1);
        System.out.println(c2.getClusterID());
        c2.removeEdge(e);
        c1.removeEdge(e);
        c2.removeDuplicateEdge(c1);//remove c4

        //add edges(c5) from c2 to c1
        for(ClusterEdge e2 : c2.getAllEdges()){
            e2.changeNeighbor(e2.getNeighborCluster(c2),c1);
            c1.addEdge(e2);
        }
        //combine demo data
        c1.combineCluster(c2);
        clusters.remove(c2.getClusterID());
        //re-compute c1 join
        for(ClusterEdge e1 : c1.getAllEdges()){
            e1.computJoin();
        }

    }

    public void run(){
            init();
        phaseone();

//        Set<ClusterEdge> e=clusters.get("42083360").getAllEdges();
//        for(ClusterEdge pe:e){
//            System.out.println(pe.getC1().getClusterID()+", "+pe.getC1().getDemo().getNATIVAAMERICAN()+" c2: "+pe.getC2().getClusterID()+", "+pe.getC2().getDemo().getNATIVAAMERICAN()+" join: "+pe.getJoinability());
//        }
//
    }
}
