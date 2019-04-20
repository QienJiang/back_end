package cse308.map.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private String clusterID;
    private Set<ClusterEdge> edges = new HashSet<>();
    private Set<Precinct> precincts;
    private Demographic demo;
    private int population;
    private boolean isAssigned;
    private String countyID;
    private String color;


    public void assignedColor(String[] color){
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(color));
        if(this.color==null){
            arrayList.remove(this.color);
            for(ClusterEdge edge : edges){
                if(edge.getC1().countyID!=this.clusterID)//remove all neighbor cluster's color
                    if(edge.getC1().color!=null)
                    arrayList.remove(edge.getC1().color);
            }
            this.color = arrayList.get(0);
        }

    }

    public Cluster(Precinct p){
        clusterID = p.getId();
        p.setParentCluster(clusterID);
        precincts = new HashSet<Precinct>();
        precincts.add(p);
    }

    public String getCountyID() {
        return countyID;
    }

    public void setCountyID(String countyID) {
        this.countyID = countyID;
    }

    public boolean isNeighbor(Cluster nei){
        for(ClusterEdge e : edges){
            if(e.getNeighborCluster(this) == nei){
                return true;
            }
        }
        return false;
    }
    public void addEdge(ClusterEdge e){
        if(!edges.contains(e)){
            edges.add(e);
        }
    }

    public void combineCluster(Cluster c){

    }

    public void setClusterID(String clusterID) {
        this.clusterID = clusterID;
    }

    public String getClusterID() {
        return clusterID;
    }

    public void addPrecinct(Precinct p){
        precincts.add(p);
        p.setParentCluster(clusterID);
    }
    public void removePrecinct(Precinct p){
        if(precincts.contains(p)){
            precincts.remove(p);
            p.setParentCluster(null);
        }
    }
    public void combineCluster(Cluster c1,Cluster c2){

    }
    public Demographic getDemo(){
        return demo;
    }
    public int getPopulation(){
        return population;
    }
    public Set<ClusterEdge> getAllEdges(){
        return edges;
    }
    public void adAll(PrecinctEdge e){

    }
    public void retainAll(PrecinctEdge e){

    }
    public void updataInfo(Cluster c1,Cluster c2){
        c1.population += c2.population;
        //demo??
        //edge
        //precincts.......
    }

    public String toString(){
        return clusterID;
    }

}
