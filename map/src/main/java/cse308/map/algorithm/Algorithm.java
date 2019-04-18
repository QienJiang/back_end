package cse308.map.algorithm;

import cse308.map.model.State;

import java.util.HashMap;
import java.util.Map;

public class Algorithm {

    Map<Integer,State> states = new HashMap<>();
    public Algorithm(String stateName,int numOfRun){
        for(int i =0; i < numOfRun;i++){
            states.put(i,new State(i,stateName));
        }
    }
    private void init(){

    }

    public void run(){

    }
}
