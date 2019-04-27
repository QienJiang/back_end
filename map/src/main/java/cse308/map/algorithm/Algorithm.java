package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;
import org.hibernate.transform.DistinctResultTransformer;

import java.sql.SQLSyntaxErrorException;
import java.util.*;

public class Algorithm {


    private PrecinctService precinctService;
    private Map<Integer, State> states = new HashMap<>();
    private State currentState;
    StringBuilder msg = new StringBuilder();
    String[] colors = {"#8B4513", "#8B0000", "#006400", "#00008B", "#FF00FF", "#2F4F4F", "#FF8C00", "#6B5B95", "#FFA07A", "#00FF7F"};
    private SocketIOClient client;

    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName, Configuration configuration, PrecinctService precinctService, SocketIOClient client) {
        if(configuration.getNumOfRun() == 1){
            this.currentState = new State(configuration);
        }else {
            for (int i = 0; i < configuration.getNumOfRun(); i++){
                states.put(i, new State(i, stateName, configuration));
            }
        }
        this.precinctService = precinctService;
        this.client = client;
    }

    private void init() {
        sendMessage("fetching precinct'state data...");
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts();
        for (Precinct p : allPrecincts) {
            currentState.addPrecinct(p);
        }
        sendMessage("Constructing Clusters...");
        for (Precinct p : currentState.getPrecincts().values()) {
            currentState.addCluster(new Cluster(p));
        }
        sendMessage("Initializing State...");
        currentState.initState();
    }



    private void graphPartition() {





        sendMessage("Phase 1 Graph partition...");
//        while (currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()) {
//            Cluster c1 = currentState.getSmallestCluster();
//            while (currentState.getTargetPopulation() > c1.getDemo().getPopulation() && currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()) {
//                ClusterEdge desireClusterEdge = c1.getBestClusterEdge();
//                if (desireClusterEdge != null) {
//                    Cluster c2 = desireClusterEdge.getNeighborCluster(c1);
//                    disconnectNeighborEdge(desireClusterEdge, c1, c2);
//                    currentState.removeCluster(c2);
//                    combine(c1, c2);
//                }
//            }
//        }
        int i=0;
        while(currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()){
            Set<String> mergedCluster = new HashSet<>();

            Iterator<Map.Entry<String,Cluster>> clusterIterator =  currentState.getClusters().entrySet().iterator();
            System.out.println(i+++", "+currentState.getClusters().size());
            while (clusterIterator.hasNext()){
                Map.Entry<String,Cluster> clusterEntry = clusterIterator.next();
                Cluster c1 = clusterEntry.getValue();
                ArrayList<ClusterEdge> desireClusterEdges = c1.getBestClusterEdge();
                for(ClusterEdge edge: desireClusterEdges){
                    if(!mergedCluster.contains(c1.getClusterID())&&currentState.getTargetPopulation() > c1.getDemo().getPopulation() && currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()){
                        Cluster c2 = edge.getNeighborCluster(c1);
                        if(!mergedCluster.contains(c2.getClusterID())){
                            disconnectNeighborEdge(edge,c1,c2);
                            combine(c1,c2);
                            mergedCluster.add(c2.getClusterID());
                            clusterIterator.remove();
                            break;
                        }
                    }
                }
            }
//            int counter=0;
//            String temp = "";
//            for (Cluster c : currentState.getClusters().values()) {
//                c.setColor(colors[counter%10]);
//                counter++;
//                for (Precinct ps : c.getPrecincts()) {
//                    temp += ps.getId() + ":" + c.getColor() + ",";
//
//                }
//            }
//            client.sendEvent("updateColor", temp);
//            temp = "";
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }


//        int i=0;
//        while (currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()) {
//            System.out.println("sssssssssssssssssssssssssssssssssssssssssss "+currentState.getClusters().size()+", "+i++);
//
////            Map<String,Cluster> currentClusters=currentState.getClusters();
//            Map<String,Cluster> mergeSet=new HashMap<String,Cluster>();
//
//            int v=0;
//            while(currentState.getClusters().size()!=mergeSet.size()){
////                HashMap<String,Cluster> mergeSet=new HashMap<String, Cluster>();
//                List<String> keyArray=new ArrayList<String>(currentState.getClusters().keySet());
//                Random r=new Random();
//                Cluster c1=currentState.getClusters().get(keyArray.get(r.nextInt(keyArray.size())));
//                System.out.println("sssssssssssssssss "+c1.getClusterID()+ ", "+currentState.getClusters().size()+", "+currentState.getClusters().size()+", "+v++);
//                if (currentState.getTargetPopulation() > c1.getDemo().getPopulation()
//                        && currentState.getClusters().size() > currentState.getConfiguration().getTargetDistricteNumber()
//                        && !checkIfCombined(c1,mergeSet)) {
//                    ClusterEdge desireClusterEdge = c1.getBestClusterEdge();
//                    if(desireClusterEdge!=null){
//                        System.out.println("c1: "+c1.getClusterID()+", "+desireClusterEdge.getJoinability());
//                        Cluster c2=desireClusterEdge.getNeighborCluster(c1);
//                        if(c2!=null){
//                        System.out.println("c2: "+c2.getClusterID());
//                        }else{
//
//                        }
//                        disconnectNeighborEdge(desireClusterEdge,c1,c2);
//                        combine(c1,c2);
//                        currentState.removeCluster(c2);
////                        else{System.out.println("wrong");}
//
////                        currentClusters.remove(c2.getClusterID());
//                    }
//                    mergeSet.put(c1.getClusterID(),c1);
//                }
////                currentClusters.remove(c1.getClusterID());
//
//            }
////            currentClusters=mergeSet;
//            currentState.setClusters(mergeSet);
//
//
//        }









    }

    public boolean checkIfCombined(Cluster currentClsuter,Map<String,Cluster> stateCluster){
            if(stateCluster.containsKey(currentClsuter.getClusterID())){
                return true;
            }

        return false;
    }




    private void disconnectNeighborEdge(ClusterEdge desireClusterEdge, Cluster c1, Cluster c2) {
        c2.removeEdge(desireClusterEdge);
        c1.removeEdge(desireClusterEdge);
        c1.removeDuplicateEdge(c2);//remove c4
    }

    private void  combine(Cluster c1, Cluster c2) {
        msg.append(c1).append(" merge into ").append(c2).append("'\n'");
        for (ClusterEdge edge : c1.getAllEdges()) { //add edges(c5) from c2 to c1
            edge.changeNeighbor(edge.getNeighborCluster(c1), c2);
//            System.out.println("c1: "+edge.getC1()+" c2: "+edge.getC2());
            c2.addEdge(edge);
        }
        c2.combineCluster(c1);//combine demo data
        for (ClusterEdge edge : c2.getAllEdges()) {//re-compute c1 join
            edge.computJoin();
        }
      //  System.exit(0);
    }

    public void annealing(){
        Map<String, District> districts = currentState.getDistricts();
        int equalPopulation=currentState.getPopulation()/currentState.getConfiguration().getTargetDistricteNumber();
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
            District neighborDistrict=currentState.getFromDistrict(otherDistrictPrecinct);
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
        for(District d:currentState.getDistricts().values()){
            score+=d.getCurrentScore();
        }
        return score;
    }

    public double rateDistrict(District d){
        double score=0;

        return score;
    }

    public double ratePopequality(District d){
        int idealPopulation=currentState.getPopulation()/currentState.getDistricts().size();
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
        for(District sd:currentState.getDistricts().values()){
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
        for(District sd:currentState.getDistricts().values()){
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

        msg.append("'\n'");
        int cn = 1;
        for (Cluster c : currentState.getClusters().values()) {
            msg.append("No." + cn + ": " + c.getClusterID() + " : precinct size " + c.getPrecincts().size() + ", population " + c.getDemo().getPopulation()).append("'\n'");
            cn++;
        }
        String temp = "";
        int counter = 0;
        sendMessage("Assigning Colors...");
        StringBuilder districtJson = new StringBuilder();
        districtJson.append("{\"type\":\"FeatureCollection\", \"features\": [");
        for (Cluster c : currentState.getClusters().values()) {
            c.setColor(colors[counter]);
            districtJson.append(c.toGeoJsonFormat()).append("},\n");
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
        districtJson.deleteCharAt(districtJson.length()-2).append("]}");
        sendDistrictBoundary(districtJson.toString());
        sendMessage(msg.toString());
        sendMessage("Algorithm finished!");
    }

    private void sendMessage(String msg) {
        client.sendEvent("message", msg);
    }
    private void sendDistrictBoundary(String msg) {
        client.sendEvent("updateDistrictBoundary", msg);
    }
}
