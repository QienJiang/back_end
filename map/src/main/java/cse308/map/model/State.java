package cse308.map.model;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "State")
public class State {

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

    public District getFromDistrict(Precinct precinct){
        return districts.get(precinct.getParentCluster());
    }

    public Cluster getSmallestCluster() {
        int i = Integer.MAX_VALUE;
        Cluster smallestCluster = null;
        for (Cluster c : clusters.values()) {
            if (c.getDemo().getPopulation() < i && c.getDemo().getPopulation() > 0) {
                smallestCluster = c;
                i = c.getDemo().getPopulation();
            }
        }
        return smallestCluster;
    }

    public int getTargetPopulation() {
        return population / clusters.size();
    }

    public void initState() {
        setPopulation(0);
        for (Precinct p : precincts.values()) {
            setPopulation((int) (population + p.getPop100()));
            Demographic demo1 = new Demographic(MajorMinor.NATIVEAMERICAN, (int) p.getPop100(), p.getNativeamericanpop(),p.getDEMVote(),p.getGOPVote());
            p.setDemo(demo1);
            String[] neighbors = p.getNeighbors().split(",");
            Cluster c1 = clusters.get(p.getId());
            c1.setCountyID(p.getCountyfp10());
            c1.setDemo(p.getDemo());
            for (String name : neighbors) {
                Precinct neighbor = precincts.get(name);
                if (!p.isNeighbor(neighbor)) {
                    Demographic demo2 = new Demographic(MajorMinor.NATIVEAMERICAN, (int) neighbor.getPop100(), neighbor.getNativeamericanpop(),neighbor.getDEMVote(),neighbor.getGOPVote());
                    neighbor.setDemo(demo2);
                    PrecinctEdge precinctEdge = new PrecinctEdge(p, neighbor);
                    precinctEdge.computJoin();
                    p.addEdge(precinctEdge);
                    neighbor.addEdge(precinctEdge);
                    Cluster c2 = clusters.get(neighbor.getId());
                    c2.setCountyID(neighbor.getCountyfp10());
                    c2.setDemo(neighbor.getDemo());
                    ClusterEdge clusterEdge = new ClusterEdge(c1, c2);
                    clusterEdge.setJoinability(precinctEdge.getJoinability());
                    c1.addEdge(clusterEdge);
                    c2.addEdge(clusterEdge);
                }
            }
        }
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