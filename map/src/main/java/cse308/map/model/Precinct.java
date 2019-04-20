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
    private double pop100;//population
    private String countyfp10;//countyID
    private String neighbors;
    @Transient private int parentCluster;
    @Transient private Set<Edge> edges;
    @Transient private Demographic demo;
    @Transient private boolean iscomput=false;

    public double getPop100() {
        return pop100;
    }

    public void setPop100(double pop100) {
        this.pop100 = pop100;
    }

    public String getGeoid10() {
        return geoid10;
    }

    public void setGeoid10(String geoid10) {
        this.geoid10 = geoid10;
    }



    public String getCountyfp10() {
        return countyfp10;
    }

    public void setCountyfp10(String countyfp10) {
        this.countyfp10 = countyfp10;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public boolean isIscomput() {
        return iscomput;
    }

    public void setIscomput(boolean iscomput) {
        this.iscomput = iscomput;
    }
//    public string getNeighbors(){
//    return neighbors;
//    }

    public void computeNeighbor(){
        if(neighbors!=null)
          //  neiPs = neighbors.split(",");
            iscomput = true;
    }
    public void isCompute(){

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
    public boolean isNeighbor(Precinct nei){
        for(Edge e :edges){
            if(e.getNeighborPrecinct(this) == nei){
                return true;
            }
        }
        return false;
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
