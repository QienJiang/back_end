package cse308.map.model;

public class PrecinctEdge {
    private double comactness;
    private Precinct p1;
    private Precinct p2;
    private double communityifInterset;
    private double countyJoinability;
    private double joinability;

    public Precinct getNeighborPrecinct(Precinct p){
        if(p1.equals(p)){
            return p2;
        }
        return null;
    }
    public PrecinctEdge(Precinct p1, Precinct p2){
        this.p1 = p1;
        this.p2 = p2;

    }
    public double computJoin(){
        int totalPopulation =p1.getDemo().getPopulation() + p2.getDemo().getPopulation();

        double join = 10.2;
        return join;//????
    }
    public void assignJoin(){
        //????????
    }



}
