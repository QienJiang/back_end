package cse308.map.model;

public class ClusterEdge {
    private double comactness;
    private Cluster c1;
    private Cluster c2;

    public double getComactness() {
        return comactness;
    }

    public void setComactness(double comactness) {
        this.comactness = comactness;
    }

    public Cluster getC1() {
        return c1;
    }

    public void setC1(Cluster c1) {
        this.c1 = c1;
    }

    public Cluster getC2() {
        return c2;
    }

    public void setC2(Cluster c2) {
        this.c2 = c2;
    }

    public double getCommunityifInterset() {
        return communityifInterset;
    }

    public void setCommunityifInterset(double communityifInterset) {
        this.communityifInterset = communityifInterset;
    }

    public double getCountyJoinability() {
        return countyJoinability;
    }

    public void setCountyJoinability(double countyJoinability) {
        this.countyJoinability = countyJoinability;
    }

    public void setJoinability(double joinability) {
        this.joinability = joinability;
    }

    private double communityifInterset;
    private double countyJoinability;
    private double joinability;


    public Cluster getNeighborCluster(Cluster p){
        if(c1.equals(p)){
            return c2;
        }
        return null;
    }
    public ClusterEdge(Cluster c1, Cluster c2){
        this.c1 = c1;
        this.c2 = c2;
    }
    public void computJoin(){
        int totalPopulation = c1.getDemo().getPopulation() + c2.getDemo().getPopulation();
        int totalAFRICAN_AMERICAN = c1.getDemo().getAFRICAN_AMERICAN() + c2.getDemo().getAFRICAN_AMERICAN();
        int totalASIAN_PACIFIC = c1.getDemo().getASIAN_PACIFIC() + c2.getDemo().getASIAN_PACIFIC();
        int totalHISPANIC = c1.getDemo().getHISPANIC() + c2.getDemo().getHISPANIC();
        int totalLATINO = c1.getDemo().getLATINO() + c2.getDemo().getLATINO();
        double joinability = 0;
        switch (c1.getDemo().getMajorMinor()) {
            case AFRICAN_AMERICAN:
                joinability = totalAFRICAN_AMERICAN / totalPopulation;
                break;
            case ASIAN_PACIFIC:
                joinability = totalASIAN_PACIFIC / totalPopulation;
                break;
            case HISPANIC:
                joinability = totalLATINO / totalPopulation;
                break;
            case LATINO:
                joinability = totalLATINO / totalPopulation;
                break;
        }
        setJoinability(joinability);
        
    }
    public void assignJoin(){
        //????????
    }
    public double getJoinability(){

        return joinability;
    }

    public String toString(){
        return c1.getClusterID() + ": " + c2.getClusterID();
    }
}
