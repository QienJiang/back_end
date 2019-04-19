package cse308.map.model;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private int clusterID;
    private Set<ClusterEdge> edges;
    private Set<Precinct> precincts;
    private Demographic demo;
    private int population;
    private boolean isAssigned;

    public void initCluster(Precinct p){
        p.setParentCluster(clusterID);
        precincts =new HashSet<Precinct>();
        precincts.add(p)
;    }

    public void initClusterEdge(Precinct p){
        precincts.add(p);
    }



    public void combineCluster(Cluster c){

    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void addPrecinct(Precinct p){
        precincts.add(p);
        p.setParentCluster(clusterID);
    }
    public void removePrecinct(Precinct p){
        if(precincts.contains(p)){
            precincts.remove(p);
            p.setParentCluster(-1);
        }
    }
    public void combineCluster(Cluster c1,Cluster c2){

    }
    public Demographic getDemo(Cluster c){
        return c.demo;
    }
    public int getPopulation(){
        return population;
    }
    public Set<ClusterEdge> getAllEdges(){
        return edges;
    }
    public void adAll(Edge e){

    }
    public void retainAll(Edge e){

    }
    public void updataInfo(Cluster c1,Cluster c2){
        c1.population += c2.population;
        //demo??
        //edge
        //precincts.......
    }

}
