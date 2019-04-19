package cse308.map.model;

public class District {
    private Cluster cluster;
    private int DistrictID;
    private Precinct precinct;
    private String population;

    public District(){

    }
    public void removePrecinct(Precinct precinct){
        cluster.removePrecinct(precinct);
    }
    public void addPrecinct(Precinct precinct){
        cluster.addPrecinct(precinct);

    }

}
