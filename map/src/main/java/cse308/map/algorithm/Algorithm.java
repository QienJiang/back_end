package cse308.map.algorithm;

import cse308.map.model.PrecinctEdge;
import cse308.map.model.Precinct;
import cse308.map.model.State;
import cse308.map.server.PrecinctService;

import java.util.HashMap;
import java.util.Map;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private Map<String,Precinct> precincts = new HashMap<>();

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

        for(Precinct p :precincts.values()){
            String[] neighbours =  p.getNeighbors().split(",");
            for(String name: neighbours){
                Precinct neighbour = precincts.get(name);
                if(!p.isNeighbor(neighbour)) {
                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbour);
                    p.addEdge(precinctEdge);
                    neighbour.addEdge(precinctEdge);
                }
            }
        }

    }

    public void run(){
            init();
    }
}
