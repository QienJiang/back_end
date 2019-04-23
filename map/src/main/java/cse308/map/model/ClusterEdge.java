package cse308.map.model;

public class ClusterEdge {
    private double comactness;
    private Cluster c1;
    private Cluster c2;

    private double communityifInterset;

    private double joinability;

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


    public void setJoinability(double joinability) {
        this.joinability = joinability;
    }

    public Cluster getNeighborCluster(Cluster p) {
        if (c1 == p) {
            return c2;
        } else if (c2 == p) {
            return c1;
        }
        return null;
    }

    public void changeNeighbor(Cluster p, Cluster neighbor) {
        if (c1 == p) {
            c2 = neighbor;
        } else {
            c1 = neighbor;
        }
    }

    public ClusterEdge(Cluster c1, Cluster c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public void computJoin() {
        int totalPopulation = c1.getDemo().getPopulation() + c2.getDemo().getPopulation();
        double joinability = 0;
        int countyValue = c1.getCountyID().equals(c2.getCountyID()) ? 1 : 0;
        switch (c1.getDemo().getMajorMinor()) {
            case AFRICAN_AMERICAN:
                int totalAFRICAN_AMERICAN = c1.getDemo().getAFRICAN_AMERICAN() + c2.getDemo().getAFRICAN_AMERICAN();
                joinability = (double) totalAFRICAN_AMERICAN / totalPopulation;
                break;
            case ASIAN_PACIFIC:
                int totalASIAN_PACIFIC = c1.getDemo().getASIAN_PACIFIC() + c2.getDemo().getASIAN_PACIFIC();
                joinability = (double) totalASIAN_PACIFIC / totalPopulation;
                break;
            case HISPANIC:
                int totalHISPANIC = c1.getDemo().getHISPANIC() + c2.getDemo().getHISPANIC();
                joinability = (double) totalHISPANIC / totalPopulation;
                break;
            case LATINO:
                int totalLATINO = c1.getDemo().getLATINO() + c2.getDemo().getLATINO();
                joinability = (double) totalLATINO / totalPopulation;
                break;
            case NATIVEAMERICAN:
                int totalNative = c1.getDemo().getNATIVAAMERICAN() + c2.getDemo().getNATIVAAMERICAN();
                joinability = (double) totalNative / totalPopulation;
                break;
        }
        joinability = joinability * 0.5 + countyValue * 0.5;
        setJoinability(joinability);

    }

    public void assignJoin() {
        //????????
    }

    public double getJoinability() {

        return joinability;
    }

    public String toString() {
        return c1.getClusterID() + ": " + c2.getClusterID();
    }
}
