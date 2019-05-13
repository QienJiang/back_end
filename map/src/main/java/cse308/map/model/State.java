package cse308.map.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "State")
public class State implements Serializable {

    @Id
    private String id;
    private String name;
    private int population;
    private String rvote;
    private String dvote;
    @Transient
    private Map<String, Precinct> precincts = new HashMap<>();
    @Transient
    private Map<String, Cluster> clusters = new HashMap<>();

    public Map<String, District> getDistricts() {
        return districts;
    }

    public void setDistricts(Map<String, District> districts) {
        this.districts = districts;
    }

    @Transient
    private Map<String, District> districts = new HashMap<>();
    @Transient
    private Configuration configuration;

    public State(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addPrecinct(Precinct p) {
        precincts.put(p.getId(), p);
    }

    public void addCluster(Cluster c) {
        clusters.put(c.getClusterID(), c);
    }

    public void removeCluster(Cluster c) {
        clusters.remove(c.getClusterID());
    }

    public State(Integer id, String name, Configuration configuration) {
        this.id = id.toString();
        this.name = name;
        this.configuration = configuration;
    }

    public MajorMinor getComunityOfinterest(){
        return configuration.getCommunityOfInterest();
    }

    public District getFromDistrict(Precinct precinct){
        return districts.get(precinct.getParentCluster());
    }

    public Cluster getSmallestCluster() {
        int i = Integer.MAX_VALUE;
        Cluster smallestCluster = null;
        for (Cluster c : clusters.values()) {
            if (c.getDemographic().getPopulation() < i && c.getDemographic().getPopulation() > 0) {
                smallestCluster = c;
                i = c.getDemographic().getPopulation();
            }
        }
        return smallestCluster;
    }

    public int getTargetPopulation() {
        return population / clusters.size();
    }

    public void initState() {
        int totalPopulation = 0;
        for (Precinct p : precincts.values()) {
            totalPopulation += p.getDemographic().getPopulation();
            String[] neighbors = p.getNeighbors().split(",");
            Cluster c1 = clusters.get(p.getId());
            c1.setCountyID(p.getCounty());
            c1.setDemographic(p.getDemographic());
            for (String name : neighbors) {
                Precinct neighbor = precincts.get(name);
                if (!p.isNeighbor(neighbor)) {
                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbor,configuration.getCommunityOfInterest(),configuration.getMajorMinorWeight());
                    p.addEdge(precinctEdge);
                    neighbor.addEdge(precinctEdge);
                    Cluster c2 = clusters.get(neighbor.getId());
                    c2.setCountyID(neighbor.getCounty());
                    c2.setDemographic(neighbor.getDemographic());
                    ClusterEdge clusterEdge = new ClusterEdge(c1, c2,configuration.getCommunityOfInterest(),configuration.getMajorMinorWeight());
                    c1.addEdge(clusterEdge);
                    c2.addEdge(clusterEdge);
                }
            }
        }
        population = totalPopulation;
    }

    public String getRvote() {
        return rvote;
    }

    public void setRvote(String rvote) {

        this.rvote = rvote;
    }

    public String getDvote() {

        return dvote;
    }

    public void setDvote(String dvote) {

        this.dvote = dvote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Map<String, Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Map<String, Precinct> precincts) {
        this.precincts = precincts;
    }

    public Map<String, Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Map<String, Cluster> clusters) {
        this.clusters = clusters;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public  void initDistrict(){
        for(Cluster c:clusters.values()){
            districts.put(c.getClusterID(),new District(c));
        }
    }

}