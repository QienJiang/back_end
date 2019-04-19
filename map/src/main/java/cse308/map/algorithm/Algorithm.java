package cse308.map.algorithm;

import cse308.map.model.Precinct;
import cse308.map.model.State;
import cse308.map.server.PrecinctService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Algorithm {
    @Autowired
    private PrecinctService precinctService;
    private Map<Integer,State> states = new HashMap<>();
    private Map<String,Precinct> precincts = new HashMap<>();

    public Algorithm(String stateName,int desireDistrict,int numOfRun){
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
    }
    private void init(){
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        for(Precinct p : allPrecincts){
            precincts.put(p.getId(),p);
        }


    }

    public void run(){

    }
}
