package cse308.map.algorithm;

import cse308.map.model.Precinct;
import cse308.map.model.State;
import cse308.map.server.PrecinctService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
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
        if(allPrecincts == null){
            System.out.println("xxxxxxxxxxxx");
        }
        for(Precinct p : allPrecincts){
            precincts.put(p.getId(),p);
            System.out.println("precinct id: "+p.getId());
        }


    }

    public void run(){
            init();
    }
}
