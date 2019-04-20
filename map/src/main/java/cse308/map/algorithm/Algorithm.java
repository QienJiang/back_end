package cse308.map.algorithm;

import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import java.util.HashMap;
import java.util.Map;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private Map<String,Precinct> precincts = new HashMap<>();
    private Map<String, Cluster> clusters = new HashMap<>();

    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName,int desireDistrict,int numOfRun,PrecinctService precinctService ){
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
        this.precinctService = precinctService;
    }
    private void init(){
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        for(Precinct p : allPrecincts){
            precincts.put(p.getId(),p);
        }

        for(Precinct p : precincts.values()){
            clusters.put(p.getId(),new Cluster(p));
        }

        for(Precinct p :precincts.values()){
            String[] neighbors =  p.getNeighbors().split(",");
            for(String name: neighbors){
                Precinct neighbor = precincts.get(name);
                if(!p.isNeighbor(neighbor)) {
                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbor);
                    p.addEdge(precinctEdge);
                    neighbor.addEdge(precinctEdge);
                    precinctEdge.computJoin();//compute joinability of the two precincts

                    Cluster c1 = clusters.get(p.getId());
                    Cluster c2 = clusters.get(neighbor.getId());
                    ClusterEdge clusterEdge = new ClusterEdge(c1,c2);
                    c1.addEdge(clusterEdge);
                    c2.addEdge(clusterEdge);
                    clusterEdge.computJoin();//compute joinability of the two precincts
                    System.out.println(clusterEdge);
                }
            }
        }

    }

    public void run(){
            init();
    }
}
