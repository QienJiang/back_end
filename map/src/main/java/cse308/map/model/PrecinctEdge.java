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
        int totalAFRICAN_AMERICAN = p1.getDemo().getAFRICAN_AMERICAN() + p2.getDemo().getAFRICAN_AMERICAN();
        int totalASIAN_PACIFIC = p1.getDemo().getASIAN_PACIFIC() + p2.getDemo().getASIAN_PACIFIC();
        int totalHISPANIC = p1.getDemo().getHISPANIC() + p2.getDemo().getHISPANIC();
        int totalLATINO = p1.getDemo().getLATINO() + p2.getDemo().getLATINO();
        int totalNative = p1.getDemo().getNATIVAAMERICAN() + p2.getDemo().getNATIVAAMERICAN();
        double joinability = 0;
        int countyValue = p1.getCountyfp10().equals(p2.getCountyfp10()) ? 1 : 0;
        switch (p1.getDemo().getMajorMinor()) {
            case AFRICAN_AMERICAN:
                joinability = totalAFRICAN_AMERICAN / totalPopulation;
                break;
            case ASIAN_PACIFIC:
                joinability = (double) totalASIAN_PACIFIC / totalPopulation;
                break;
            case HISPANIC:
                joinability = (double) totalHISPANIC / totalPopulation;
                break;
            case LATINO:
                joinability = (double) totalLATINO / totalPopulation;
                break;
            case NATIVEAMERICAN:
                joinability = (double) totalNative / totalPopulation;
                break;
        }
        joinability = joinability * 0.5 + countyValue * 0.5;
        setJoinability(joinability);
    }

    public void assignJoin() {
        //????????
    }


}
