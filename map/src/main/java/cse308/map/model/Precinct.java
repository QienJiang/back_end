package cse308.map.model;

import java.util.Set;
import javax.persistence.Id;

public class Precinct {
    @Id
    private int parentCluster;
    private Set<Edge> edges;
    private Demographic demo;
    private int population;
    private int countyID;
    private String id;
    public Precinct(){

    }
    public void computeNeighbor(){

    }
    public void isCompute(){

    }
    public boolean isNeighbor(Precinct cp2){
        return true;
    }
    public void assignNeighbor(Precinct cp2){

    }
    public void computeCountyJoin(int countyID) {

    }

    public int getParentCluster() {
        return parentCluster;
    }
    public void computeJoinability(){

    }
    public int getNeighborCounty(){
        return 0;
    }
    public Demographic getDemo(){
        return demo;
    }
    public Demographic getNeighborDemo(){
        return demo;
    }
    public void compute(){

    }
    public Set<Edge> getAllEdges(){
        return edges;
    }
    public String getId() {
        return id;
    }
}
