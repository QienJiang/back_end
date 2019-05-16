package cse308.map.model;

import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class District implements Comparable<District>,Serializable {
    private String districtID;
    private Cluster cluster;
    private double compactness;
    private Party p;

    public double getCompactness() {
        return compactness;
    }

    public void setCompactness(double compactness) {
        this.compactness = compactness;
    }

    public Geometry getShape() {
        return cluster.getShape();
    }

    public void setShape(Geometry shape) {
        cluster.setShape(shape);
    }

    public District(Cluster c) {
        cluster=c;
        districtID=cluster.getClusterID();
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public void setDemo(Demographic demo) {
        cluster.setDemographic(demo);
    }

    public double getCurrentScore(){
        return cluster.getCurrentScore();
    }

    public void setCurrentScore(double score){
        cluster.setCurrentScore(score);
    }


    public String getdistrictID() { return districtID; }

    public void addPrecinct(Precinct p) {
//        if(!this.cluster.getPrecincts().contains(p)) {
            this.cluster.addPrecinct(p);
            p.setParentCluster(districtID);
            Geometry newDistrict = this.getCluster().getShape().union(p.getShape());
            this.getCluster().setShape(newDistrict);
            this.getCluster().getDemographic().combineDemo(p.getDemographic());
//        }
    }

    public void removePrecinct(Precinct p) {
//        if (this.cluster.getPrecincts().contains(p)) {
            this.cluster.removePrecinct(p);
            Geometry newDistrict = this.getCluster().getShape().symDifference(p.getShape());
            this.getCluster().setShape(newDistrict);
            this.getCluster().getDemographic().removeDemo(p.getDemographic());
            if(this.getCluster().getDemographic().getPopulation()<=0){
                System.out.println("FROM:"+this.getdistrictID());
                System.exit(0);
            }
        p.setParentCluster(null);
//        }
    }

    public Demographic getDemo() {
        return cluster.getDemographic();
    }

    public double getMajorMinor(MajorMinor communityOfInterest){
        double majorMinorValue = 0;
        int totalPopulation = this.getCluster().getDemographic().getPopulation();
        int totalMmPopulation = this.getCluster().getDemographic().getMajorMinorPop(communityOfInterest);
        majorMinorValue = (double) totalMmPopulation / totalPopulation;
        return majorMinorValue;
    }

    public String toString() {
        return districtID;
    }

    public Set<Precinct> getBorderPrecincts() {

        Set<Precinct> borderPrecincts = new HashSet<Precinct>();

        for (Precinct precinct : cluster.getPrecincts()) {
                if(precinct.isBorderPrecinct()){
                    borderPrecincts.add(precinct);
                }
        }
        return borderPrecincts;
    }

    public Party getP() {
        return p;
    }

    public void setParty() {
        if(this.getGOPVote()>= this.getDEMVote()){
            p = Party.REPUBLICAN;
        }else{
            p = Party.DEMOCRATIC;
        }
    }

    public int getPopulation() {
        return cluster.getDemographic().getPopulation();
    }

    public int getGOPVote() {
        return cluster.getDemographic().getRepublicanVote();
    }

    public int getDEMVote() {
        return cluster.getDemographic().getDemocraticVote();
    }

    @Override
    public int compareTo(District o) {
        if(cluster.getDemographic().getPopulation() == o.getCluster().getDemographic().getPopulation()){
            return  0;
        }else if(cluster.getDemographic().getPopulation() > o.getCluster().getDemographic().getPopulation()){
            return 1;
        }
        return -1;
    }
}
