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
    private String geoid10;//precinct id
    private int pop100;//population
    private String countyfp10;//countyID
    private String neighbors;
    @Transient private int parentCluster;
    @Transient private Set<Edge> edges;
    @Transient private Demographic demo;
    @Transient private String[] neiPs;
    @Transient private boolean iscomput=false;

//    public string getNeighbors(){
//    return neighbors;
//    }

    public void computeNeighbor(){
        if(neighbors!=null)
            neiPs = neighbors.split(",");
            iscomput = true;
    }
    public void isCompute(){
        
    }
    public boolean isNeighbor(Precinct p1,Precinct p2){
        boolean isN = false;
        for(Edge e : edges){
            if(e.getNeighborPrecinct(p1).equals(p2)){
                isN = true;
            }
        }
        return isN;
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
        return geoid10;
    }
    public String getNeighbors(){return neighbors;}
    public void setNeighbors(String neighbors){
        this.neighbors = neighbors;
    }
    public void addEdge(Edge e){
        if(!edges.contains(e)){
            edges.add(e);
        }
    }
    public void removeEdge(Edge e){
        if(edges.contains(e)){
            edges.remove(e);
        }
    }
    public void setDemo(Demographic demo){
        this.demo = demo;
    }


}
