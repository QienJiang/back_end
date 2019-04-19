package cse308.map.model;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pa_finalqgis")
public class Precinct {
    @Id
    private String id;
//    private string neighbors;
@Transient private int parentCluster;
    @Transient private Set<Edge> edges;
    @Transient private Demographic demo;
    @Transient private String population;
    @Transient private int countyID;

//    public string getNeighbors(){
//    return neighbors;
//    }

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
    public void setParentCluster(int cID){
        parentCluster = cID;
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
