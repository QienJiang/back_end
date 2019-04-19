package cse308.map.model;

public class ClusterEdge {
    private double comactness;
    private Cluster c1;
    private Cluster c2;
    private double communityifInterset;
    private double countyJoinability;
    private double joinability;
    private ClusterEdge e;


    public ClusterEdge(){

    }
    public Cluster getNeighborCluster(Cluster p){
        if(c1.equals(p)){
            return c2;
        }
        return null;
    }
    public ClusterEdge initialEdge(Cluster c1, Cluster c2){
        this.c1 = c1;
        this.c2 = c2;

        return e;
    }
    public double computJoin(){
        double join = 10.2;
        return join;//????
    }
    public void assignJoin(){
        //????????
    }
    public double getJoinability(){

        return joinability;
    }

}
