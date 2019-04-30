package cse308.map.model;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.HashSet;
import java.util.Set;
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.io.geojson.GeoJsonWriter;
//
//import java.util.ArrayList;
//import java.util.Arrays;

public class District implements Comparable<District>{
    /*
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
    */
    private String districtID;
    private Cluster cluster;
    private double compactness;
    private Geometry shape;


//    public double calCompactness(){
//        int allPrecincts = d.getCluster().getPrecincts().size();
//        int borderPrecincts = d.getBorderPrecincts().size();
//        return borderPrecincts/(allPrecincts-borderPrecincts);
//    }


    public double getCompactness() {
        return compactness;
    }

    public void setCompactness(double compactness) {
        this.compactness = compactness;
    }

    public Geometry getShape() {
        return shape;
    }

    public void setShape(Geometry shape) {
        this.shape = shape;
    }

    public District(Cluster c) {
        cluster=c;
        districtID=cluster.getClusterID();
        shape = new GeometryFactory().createGeometry(cluster.getShape());
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


    //    public void assignedColor(String[] color) {
//        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(color));
//        if (this.color == null) {
//            arrayList.remove(this.color);
//            for (ClusterEdge edge : edges) {
//                if (edge.getC1().countyID != this.districtID)//remove all neighbor cluster's color
//                    if (edge.getC1().color != null)
//                        arrayList.remove(edge.getC1().color);
//            }
//            this.color = arrayList.get(0);
//        }
//
//    }




//    public Set<Precinct> getPrecincts() {
//        return precincts;
//    }
//
//    public void setPrecincts(Set<Precinct> precincts) {
//        this.precincts = precincts;
//    }
//
    public void setDemo(Demographic demo) {
        cluster.setDemographic(demo);
    }
    public double getCurrentScore(){
        return cluster.getCurrentScore();
    }

    public void setCurrentScore(double score){
        cluster.setCurrentScore(score);
    }
//
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//
//    public District(Cluster cluster) {
//        districtID = cluster.getClusterID();
//        shape = new GeometryFactory().createGeometry(cluster.getShape());
//        precincts = new HashSet<Precinct>();
//        precincts.addAll(cluster.getPrecincts());
//    }

//    public boolean isNeighbor(District nei) {
//        for (ClusterEdge e : edges) {
//            if (e.getNeighborCluster(this) == nei) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void addEdge(ClusterEdge e) {
//        if (!edges.contains(e)) {
//            edges.add(e);
//        }
//    }
//
//    public void removeEdge(ClusterEdge e) {
//        if (edges.contains(e)) {
//            edges.remove(e);
//        }
//    }

//    public void removeDuplicateEdge(Cluster c1) {
//        Set<ClusterEdge> remove = new HashSet<>();
//        for (ClusterEdge e1 : c1.getAllEdges()) {
//            for (ClusterEdge e2 : edges) {
//                Cluster c4 = e2.getNeighborCluster(this);
//                if (c4 != null) {
//                    if (e1.getNeighborCluster(c1) == c4) {
//                        System.out.println("c4: " + c4.getdistrictID());
//                        c4.removeEdge(e2);
//                        remove.add(e2);
//                    }
//                }
//            }
//        }
//        edges.removeAll(remove);
//    }

//    public ClusterEdge getBestClusterEdge() {
//        double maxjoin = 0;
//        ClusterEdge desireClusterEdge = null;
//        for (ClusterEdge e : edges) {
//            Cluster c2 = e.getNeighborCluster(this);
//            System.out.println("2: " + c2.getdistrictID() + ", " + e.getJoinability());
//            if (maxjoin < e.getJoinability()) {
//                maxjoin = e.getJoinability();
//                desireClusterEdge = e;
//            }
//        }
//        return desireClusterEdge;
//    }


    public void setdistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getdistrictID() {
        return districtID;
    }

    public void addPrecinct(Precinct p) {
        cluster.addPrecinct(p);
        p.setParentCluster(districtID);
    }

    public void removePrecinct(Precinct p) {
        if (cluster.getPrecincts().contains(p)) {
            cluster.removePrecinct(p);
            p.setParentCluster(null);
        }
    }
//
//
    public Demographic getDemo() {
        return cluster.getDemographic();
    }

    public double getMajorMinor(){
        double majorMinorValue = 0;
        int totalPopulation = this.getCluster().getDemographic().getPopulation();
        int totalMmPopulation = this.getCluster().getDemographic().getNativeAmerican();
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
