package cse308.map.model;

public class PrecinctEdge {
    private double comactness;
    private Precinct p1;
    private Precinct p2;
    private double communityifInterset;
    private double countyJoinability;
    private double joinability;

    public Precinct getNeighborPrecinct(Precinct p) {
        if (p1 == p) {
            return p2;
        } else if (p2 == p) {
            return p1;
        }
        return null;
    }

    public PrecinctEdge(Precinct p1, Precinct p2) {
        this.p1 = p1;
        this.p2 = p2;

    }

    public double getComactness() {
        return comactness;
    }

    public void setComactness(double comactness) {
        this.comactness = comactness;
    }

    public Precinct getP1() {
        return p1;
    }

    public void setP1(Precinct p1) {
        this.p1 = p1;
    }

    public Precinct getP2() {
        return p2;
    }

    public void setP2(Precinct p2) {
        this.p2 = p2;
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

    public double getJoinability() {
        return joinability;
    }

    public void setJoinability(double joinability) {
        this.joinability = joinability;
    }

    public void computJoin() {
        int totalPopulation = p1.getDemo().getPopulation() + p2.getDemo().getPopulation();
        int totalMmPopulation=p1.getDemo().getMmPopulation()+p2.getDemo().getMmPopulation();
        double majorMinorValue=(double) totalMmPopulation/totalPopulation;
        int countyValue = p1.getCountyfp10().equals(p2.getCountyfp10()) ? 1 : 0;
        this.joinability = majorMinorValue * 0.5 + countyValue * 0.5;
        setJoinability(joinability);
    }

    public void assignJoin() {
        //????????
    }


}
