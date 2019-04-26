package cse308.map.model;

import com.vividsolutions.jts.geom.Geometry;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "pa_finalqgis")
public class Precinct {
    @Id
    private String geoid10;//precinct id
    private Geometry shape;
    private double pop100;//population
    private String countyfp10;//countyID
    private String neighbors;
    private int nativeamericanpop;
    private int GOPVote;
    private int DEMVote;
    @Transient
    private String parentCluster;
    @Transient
    private Set<PrecinctEdge> precinctEdges = new HashSet<>();
    @Transient
    private Demographic demo;
    @Transient
    private boolean iscomput = false;

    public double getPop100() {
        return pop100;
    }

    public void setPop100(double pop100) {
        this.pop100 = pop100;
    }

    public String getGeoid10() {
        return geoid10;
    }

    public void setGeoid10(String geoid10) {
        this.geoid10 = geoid10;
    }

    public int getNativeamericanpop() {
        return nativeamericanpop;
    }

    public void setNativeamericanpop(int nativeamericanpop) {
        this.nativeamericanpop = nativeamericanpop;
    }

    public String getCountyfp10() {
        return countyfp10;
    }

    public void setCountyfp10(String countyfp10) {
        this.countyfp10 = countyfp10;
    }

    public Set<PrecinctEdge> getPrecinctEdges() {
        return precinctEdges;
    }

    public void setPrecinctEdges(Set<PrecinctEdge> precinctEdges) {
        this.precinctEdges = precinctEdges;
    }

    public boolean isIscomput() {
        return iscomput;
    }

    public void setIscomput(boolean iscomput) {
        this.iscomput = iscomput;
    }

    public void setParentCluster(String cID) {
        parentCluster = cID;
    }

    public String getParentCluster() {
        return parentCluster;
    }

    public Demographic getDemo() {
        return demo;
    }

    public Set<PrecinctEdge> getAllEdges() {
        return precinctEdges;
    }

    public String getId() {
        return geoid10;
    }

    public String getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(String neighbors) {
        this.neighbors = neighbors;
    }

    public Set<Precinct> getOtherDistrctPrecincts(){
        Set<Precinct> otherDistrctPrecincts=new HashSet<Precinct>();
        for(PrecinctEdge e:precinctEdges){
            if(!e.getNeighbor(this).getParentCluster().equals(parentCluster))
                otherDistrctPrecincts.add(e.getNeighborPrecinct(this));
        }
        return otherDistrctPrecincts;
    }


    public boolean isNeighbor(Precinct nei) {
        for (PrecinctEdge e : precinctEdges) {
            if (e.getNeighborPrecinct(this) == nei) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(PrecinctEdge e) {
        if (!precinctEdges.contains(e)) {
            precinctEdges.add(e);
        }
    }

    public void removeEdge(PrecinctEdge e) {
        if (precinctEdges.contains(e)) {
            precinctEdges.remove(e);
        }
    }

    public Geometry getShape() {
        return shape;
    }

    public void setShape(Geometry shape) {
        this.shape = shape;
    }

    public void setDemo(Demographic demo) {
        this.demo = demo;
    }

    public String toString() {
        return geoid10;
    }

    public Boolean isBorderPrecinct(){
        for(PrecinctEdge precinctEdge : precinctEdges){
            Precinct neighbor = precinctEdge.getNeighbor(this);
            if(!neighbor.getParentCluster().equals(this.getParentCluster()))
                return true;
        }
        return false;
    }


    public int getGOPVote() {
        return GOPVote;
    }

    public void setGOPVote(int GOPVote) {
        this.GOPVote = GOPVote;
    }

    public int getDEMVote() {
        return DEMVote;
    }

    public void setDEMVote(int DEMVote) {
        this.DEMVote = DEMVote;
    }
}
