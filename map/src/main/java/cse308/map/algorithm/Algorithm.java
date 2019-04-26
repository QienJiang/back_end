package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;
import org.hibernate.transform.DistinctResultTransformer;

import java.util.*;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer, State> states = new HashMap<>();
    private State state;
    StringBuilder sb = new StringBuilder();
    String[] colors = {"#8B4513", "#8B0000", "#006400", "#00008B", "#FF00FF", "#2F4F4F", "#FF8C00", "#6B5B95", "#FFA07A", "#00FF7F"};
    private SocketIOClient client;

    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName, Configuration configuration, PrecinctService precinctService, SocketIOClient client) {
        for (int i = 0; i < configuration.getNumOfRun(); i++) {
            states.put(i, new State(i, stateName, configuration));
        }
        this.state = new State(configuration);
        this.precinctService = precinctService;
        this.client = client;
    }

    private void init() {
        sendMessage("fetching precinct'state data...");
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        for (Precinct p : allPrecincts) {
            state.addPrecinct(p);
        }
        sendMessage("Construct Clusters...");
        for (Precinct p : state.getPrecincts().values()) {
            state.addCluster(new Cluster(p));
        }
        sendMessage("Creating Edge...");
        state.initState();
    }


    private void graphPartition() {
        System.out.println("phaseone");
        sendMessage("Phase 1 Graph partition...");
        while (state.getClusters().size() > state.getConfiguration().getTargetDistricteNumber()) {
//            List<String> keysAsArray = new ArrayList<String>(state.getClusters().keySet());
//            Random r = new Random();
//            Cluster c1=state.getClusters().get(keysAsArray.get(r.nextInt(keysAsArray.size())));
            Cluster c1 = state.getSmallestCluster();
            System.out.println("1: " + c1.getClusterID() + "  " + state.getClusters().size() + " " + state.getTargetPopulation() + " " + c1.getDemo().getPopulation() + " " + state.getConfiguration().getTargetDistricteNumber());
            while (state.getTargetPopulation() > c1.getDemo().getPopulation() && state.getClusters().size() > state.getConfiguration().getTargetDistricteNumber()) {
                ClusterEdge desireClusterEdge = c1.getBestClusterEdge();
                if (desireClusterEdge != null) {
                    System.out.println("3: " + desireClusterEdge.getNeighborCluster(c1));
                    Cluster c2 = desireClusterEdge.getNeighborCluster(c1);
                    disconnectNeighborEdge(desireClusterEdge, c1, c2);
                    state.removeCluster(c2);
                    combine(c1, c2);
                }
            }
        }
    }

    private void disconnectNeighborEdge(ClusterEdge desireClusterEdge, Cluster c1, Cluster c2) {
        c2.removeEdge(desireClusterEdge);
        c1.removeEdge(desireClusterEdge);
        c2.removeDuplicateEdge(c1);//remove c4
    }

    private void combine(Cluster c1, Cluster c2) {
        System.out.println("combine");
        System.out.println(c2.getClusterID());
        sb.append(c2 + " merge into " + c1).append("'\n'");
        for (ClusterEdge e2 : c2.getAllEdges()) { //add edges(c5) from c2 to c1
            e2.changeNeighbor(e2.getNeighborCluster(c2), c1);
            c1.addEdge(e2);
        }
        c1.combineCluster(c2);//combine demo data
        for (ClusterEdge e1 : c1.getAllEdges()) {//re-compute c1 join
            e1.computJoin();
        }
    }

    public void annealing(){
        Map<String, District> districts = state.getDistricts();
        int equalPopulation=state.getPopulation()/state.getConfiguration().getTargetDistricteNumber();
        while(districts.size()>0) {
            District smallestDistrict = getSmallestDistrict(districts);
            while(smallestDistrict.getPopulation()<equalPopulation){
            for (Precinct precinct : smallestDistrict.getBorderPrecincts()) {
                Move bestMove = getMove(smallestDistrict, precinct);
                if (bestMove != null)
                    bestMove.execute();

            }
        }
            districts.remove(smallestDistrict.getDistrictID());
        }
    }

    public Move getMove(District current,Precinct precinct){
        Move bestMove=null;
        double bestImprovament=0;
        for(Precinct otherDistrictPrecinct : precinct.getOtherDistrctPrecincts()){
            District neighborDistrict=state.getFromDistrict(otherDistrictPrecinct);
            Move move = new Move(current,neighborDistrict,otherDistrictPrecinct);
            double improvement = testMove(move);

            if(improvement > bestImprovament) {
                bestMove = move;
                bestImprovament = improvement;
            }
        }
        return bestMove;
    }

    public double testMove(Move move){
        if(!move.getFrom().isContigunity()){
            return 0;
        }

        double initial_score = move.getTo().getCurrentScore()+move.getFrom().getCurrentScore();
        move.execute();
        double to_score=rateDistrict(move.getTo());
        double from_score=rateDistrict(move.getFrom());
        double final_score =to_score+from_score;
        double improvement = final_score-initial_score;
//        to.setCurrentScore(to_score);
//        from.setCurrentScore(from_score);
//        p.setParentCluster(to.getdistrictID());
        move.undo();
        return improvement<=0? 0: improvement;
    }

    public District getSmallestDistrict(Map<String,District> districts) {
        int i = Integer.MAX_VALUE;
        District smallestDistrict = null;
        for (District district : districts.values()) {
            if (district.getDemo().getPopulation() < i && district.getDemo().getPopulation() > 0) {
                smallestDistrict = district;
                i = district.getDemo().getPopulation();
            }
        }
        return smallestDistrict;
    }

    public double calculateObjective(){
        double score=0;
        for(District d:state.getDistricts().values()){
            score+=d.getCurrentScore();
        }
        return score;
    }

    public double rateDistrict(District d){
        double score=0;

        return score;
    }

    public double ratePopequality(District d){
        int idealPopulation=state.getPopulation()/state.getDistricts().size();
        int truePopulation = d.getPopulation();
        double suboptimality;
        if(idealPopulation>=truePopulation){
            return ((double)truePopulation)/idealPopulation;
        }
        return ((double)idealPopulation/truePopulation);
    }

    public double ratePartisanFairness(District d){
        int totalVote=0;
        int totalGOPvote=0;
        int totalDistricts=0;
        int totalGOPDistricts=0;
        for(District sd:state.getDistricts().values()){
            totalVote+=sd.getGOPVote();
            totalVote+=sd.getDEMVote();
            totalGOPvote+=sd.getGOPVote();
            totalDistricts+=1;
            if(sd.getGOPVote()>sd.getDEMVote()){
                totalGOPDistricts+=1;
            }
        }
        int idealDistrictChange=((int)Math.round(totalDistricts*((1.0*totalGOPvote)/totalVote)))-totalGOPDistricts;
        if(idealDistrictChange==0){
            return 1.0;
        }
        int gv=d.getGOPVote();
        int dv=d.getDEMVote();
        int tv=gv+dv;
        int margin=gv-dv;
        if(tv==0){
            return 1.0;
        }
        int win_v=Math.max(gv,dv);
        int loss_v=Math.min(gv,dv);
        int inefficient_v;
        if(idealDistrictChange*margin>0){
            inefficient_v=win_v-loss_v;
        } else{
            inefficient_v=loss_v;
        }
        return 1.0-((inefficient_v*1.0)/tv);
    }

    public double rateCompactness(District d){
        return 0;
    }

    public double rateStatewideEfficiencyGap(District d){
        int iv_g=0;
        int iv_d=0;
        int tv=0;
        for(District sd:state.getDistricts().values()){
            int gv=sd.getGOPVote();
            int dv=sd.getDEMVote();
            if(gv>dv){
                iv_d+=dv;
                iv_g+=gv-dv;
            } else if(dv>gv){
                iv_g+=gv;
                iv_d+=dv-gv;
            }
            tv+=gv;
            tv+=dv;
        }
        return 1.0-((Math.abs(iv_g-iv_d)*1.0)/tv);
    }

    public double rateEfficiencyGap(District d){
        int gv=d.getGOPVote();
        int dv=d.getDEMVote();
        int tv=gv+dv;
        if(tv==0){
            return 1.0;
        }
        int win_v=Math.max(gv,dv);
        int loss_v=Math.min(gv,dv);
        int inefficient_v=Math.abs(loss_v-(win_v-loss_v));
        return 1.0-((inefficient_v*1.0)/tv);
    }

    public double rateCompetitiveness(District d){
        int gv=d.getGOPVote();
        int dv=d.getDEMVote();
        return 1.0-(Math.abs(gv-dv)/(gv+dv));
    }







    public void run() {
        sendMessage("Algorithm Start...");
        init();
        graphPartition();
//        state.initDistrict();
//        annealing();

        sb.append("'\n'");
        int pn = 0;
        int cn = 1;
        for (Cluster c : state.getClusters().values()) {
            System.out.println(c.getClusterID() + " : precinct size " + c.getPrecincts().size() + ", population " + c.getDemo().getPopulation());
            sb.append("No." + cn + ": " + c.getClusterID() + " : precinct size " + c.getPrecincts().size() + ", population " + c.getDemo().getPopulation()).append("'\n'");
            pn += c.getPrecincts().size();
            cn++;
        }
        System.out.println("total precinct size: " + pn);
        String temp = "";
        int counter = 0;
        sendMessage("Assign Colors...");
        StringBuilder districJson = new StringBuilder();
        districJson.append("{\"type\":\"FeatureCollection\", \"features\": [");
        for (Cluster c : state.getClusters().values()) {
            c.setColor(colors[counter]);
            districJson.append(c.toGeoJsonFormat()).append("},\n");
            System.out.println("color :" + colors[counter]);
            counter++;
            for (Precinct ps : c.getPrecincts()) {
                temp += ps.getId() + ":" + c.getColor() + ",";
            }
            client.sendEvent("updateColor", temp);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            temp = "";
        }
        districJson.deleteCharAt(districJson.length()-2).append("]}");
        sendDistrictBoundary(districJson.toString());
        sendMessage(sb.toString());
        sendMessage("Algorithm finished!");
    }

    private void sendMessage(String msg) {
        client.sendEvent("message", msg);
    }
    private void sendDistrictBoundary(String msg) {
        client.sendEvent("updateDistrictBoundary", msg);
    }
}
