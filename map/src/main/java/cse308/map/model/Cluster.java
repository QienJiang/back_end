package cse308.map.model;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.geojson.GeoJsonWriter;

import javax.validation.constraints.Null;
import java.util.*;

public class Cluster {

    private String clusterID;
    private Geometry shape;
    private Set<ClusterEdge> edges = new HashSet<>();
    private Set<Precinct> precincts;
    private Demographic demographic;
    private boolean isAssigned;
    private String countyID;
    private String color;
    private double CurrentScore;


    public void assignedColor(String[] color) {
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(color));
        if (this.color == null) {
            arrayList.remove(this.color);
            for (ClusterEdge edge : edges) {
                if (edge.getC1().countyID != this.clusterID)//remove all neighbor cluster's color
                    if (edge.getC1().color != null)
                        arrayList.remove(edge.getC1().color);
            }
            this.color = arrayList.get(0);
        }

    }

    public Cluster() {
    }

    public void setEdges(Set<ClusterEdge> edges) {
        this.edges = edges;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }


    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

//    }

    public Set<ClusterEdge> getEdges() {
        return edges;
    }

    public Cluster(Precinct p) {
        clusterID = p.getId();
        shape = new GeometryFactory().createGeometry(p.getShape());
        p.setParentCluster(clusterID);
        precincts = new HashSet<Precinct>();
        precincts.add(p);
    }

    public String getCountyID() {
        return countyID;
    }

    public void setCountyID(String countyID) {
        this.countyID = countyID;
    }

    public boolean isNeighbor(Cluster nei) {
        for (ClusterEdge e : edges) {
            if (e.getNeighborCluster(this) == nei) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(ClusterEdge e) {
        if (!edges.contains(e)) {
            edges.add(e);
        }
    }

    public void removeEdge(ClusterEdge e) {
        if (edges.contains(e)) {
            edges.remove(e);
        }
    }

    public void removeDuplicateEdge(Cluster c2) {
        Set<ClusterEdge> remove = new HashSet<>();
        for (ClusterEdge e1 : c2.getAllEdges()) {
            Cluster c2Neighbor = e1.getNeighborCluster(c2);
            for (ClusterEdge e2 : edges) {
                Cluster c1Neighbor = e2.getNeighborCluster(this);//get c1 neighbor
                if (c1Neighbor != null) {
                    if (c2Neighbor == c1Neighbor) {//get c2 neighbor
//                        System.out.println("c4: " + c4.getClusterID());
                        c1Neighbor.removeEdge(e2);
                        remove.add(e2);
                    }
                }
            }
        }

        edges.removeAll(remove);


    }

    public ArrayList<ClusterEdge> getBestClusterEdge() {
        double maxjoin = 0;
        ArrayList<ClusterEdge> sortedEdges=new ArrayList<>();
        ClusterEdge desireClusterEdge = null;
        for (ClusterEdge e : edges) {
            sortedEdges.add(e);
        }
        Collections.sort(sortedEdges);
        return sortedEdges;
    }

    public void combineCluster(Cluster c) {
        if (!countyID.equals(c.getCountyID())) {
            if (demographic.getPopulation()< c.getDemographic().getPopulation()) {
                countyID = c.getCountyID();
            }
        }
        shape = shape.union(c.shape);
        demographic.combineDemo(c.getDemographic());
        precincts.addAll(c.getPrecincts());
    }

    public void setClusterID(String clusterID) {
        this.clusterID = clusterID;
    }

    public String getClusterID() {
        return clusterID;
    }

    public void addPrecinct(Precinct p) {
        precincts.add(p);
        p.setParentCluster(clusterID);
    }

    public void removePrecinct(Precinct p) {
        if (precincts.contains(p)) {
            precincts.remove(p);
            p.setParentCluster(null);
        }
    }

    public void combineCluster(Cluster c1, Cluster c2) {

    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Set<ClusterEdge> getAllEdges() {
        return edges;
    }

    public void adAll(PrecinctEdge e) {

    }

    public void retainAll(PrecinctEdge e) {

    }

    public void updataInfo(Cluster c1, Cluster c2) {
        //demo??
        //edge
        //precincts.......
    }

    public Geometry getShape() {
        return shape;
    }

    public void setShape(Geometry shape) {
        this.shape = shape;
    }

    public String toString() {
        return clusterID;
    }

    public String toGeoJsonFormat(){
        GeoJsonWriter writer = new GeoJsonWriter();
        String out = writer.write(shape);
        return "{\"type\":\"Feature\",\"geometry\":" + out + "," + getProperty();
    }

    public String getProperty(){
        StringBuilder s = new StringBuilder();
        s.append("\"properties\":{\"GEOID10\": \"").append(clusterID).append("\",").append("\"POP100\":\"").append(demographic.getPopulation()).append("\",").
                append("\"COLOR\":\"").append(color).append("\"}");
        return s.toString();
    }


    public double getCurrentScore() {
        return CurrentScore;
    }

    public void setCurrentScore(double CurrentScore) {
        this.CurrentScore = CurrentScore;
    }
}
