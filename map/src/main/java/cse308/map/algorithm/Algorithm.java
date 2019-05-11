package cse308.map.algorithm;

import com.corundumstudio.socketio.SocketIOClient;
import com.vividsolutions.jts.geom.Geometry;
import cse308.map.model.*;
import cse308.map.server.PrecinctService;

import java.lang.reflect.Method;
import java.util.*;

public class Algorithm {
    private static final HashMap<Measure, String> measures;

    static {
        measures = new HashMap<>();
        measures.put(Measure.POPULATION_EQUALITY, "ratePopequality");
        measures.put(Measure.EFFICIENCY_GAP, "rateStatewideEfficiencyGap");
        measures.put(Measure.COMPACTNESS, "rateCompactness");
        measures.put(Measure.PARTISAN_FAIRNESS, "ratePartisanFairness");
        measures.put(Measure.COMPETITIVENESS, "rateCompetitiveness");
    }

    private StringBuilder msg = new StringBuilder();
    private ArrayList<String> coloring = new ArrayList<>();
    private PrecinctService precinctService;
    private Map<Integer, State> states = new HashMap<>();
    private State currentState;
    private SocketIOClient client;
    private HashMap<Measure, Double> weights = new HashMap<>();
    private HashMap<District, Double> currentScores = new HashMap<>();

    //pass the precinctService to the algorithm object because we can't autowired precinctService for each object it is not working.
    public Algorithm(String stateName, Configuration configuration, PrecinctService precinctService, SocketIOClient client) {
        configuration.initWeights();
        if (configuration.getNumOfRun() == 1) {
            this.currentState = new State(configuration);
        } else {
            for (int i = 0; i < configuration.getNumOfRun(); i++) {
                states.put(i, new State(i, stateName, configuration));
            }
        }
        this.precinctService = precinctService;
        this.client = client;
        setWeights(configuration.getWeights());
    }

    private void init() {
        sendMessage("fetching precinct'state data...");
        Iterable<Precinct> allPrecincts = precinctService.getAllPrecincts("42");
        for (Precinct p : allPrecincts) {
            currentState.addPrecinct(p);
        }
        sendMessage("Constructing Clusters...");
        for (Precinct p : currentState.getPrecincts().values()) {
            currentState.addCluster(new Cluster(p));
        }
        sendMessage("Initializing State...");
        currentState.initState();
        for (Cluster c : currentState.getClusters().values()) {
            c.setColor(randomColor());
        }
    }
    
    private void graphPartition() {
        sendMessage("Phase 1 Graph partition...");
        while (currentState.getClusters().size() > currentState.getConfiguration().getTargetDistrictNumber()) {
            Set<String> mergedCluster = new HashSet<>();
            Iterator<Map.Entry<String, Cluster>> clusterIterator = currentState.getClusters().entrySet().iterator();
            while (clusterIterator.hasNext()) {
                Map.Entry<String, Cluster> clusterEntry = clusterIterator.next();
                Cluster currentCluster = clusterEntry.getValue();
                ArrayList<ClusterEdge> bestClusterEdges = currentCluster.getBestClusterEdges();
                for (ClusterEdge edge : bestClusterEdges) {
                    if (isValidCombine(currentCluster, mergedCluster)) {
                        Cluster neighborCluster = edge.getNeighborCluster(currentCluster);
                        if (!mergedCluster.contains(neighborCluster.getClusterID())) {
                            disconnectNeighborEdge(edge, currentCluster, neighborCluster);
                            combine(currentCluster, neighborCluster);
                            mergedCluster.add(neighborCluster.getClusterID());
                            clusterIterator.remove();
                            break;
                        }
                    }
                }
            }
            updateColor();
        }
    }

    private boolean isValidCombine(Cluster currentCluster, Set<String> mergedCluster) {
        return !mergedCluster.contains(currentCluster.getClusterID())//if the cluster already combine
                && currentState.getTargetPopulation() > currentCluster.getDemographic().getPopulation()
                && currentState.getClusters().size() > currentState.getConfiguration().getTargetDistrictNumber();
    }

    private void updateColor() {
        StringBuilder temp = new StringBuilder();
        for (Cluster c : currentState.getClusters().values()) {
            for (Precinct ps : c.getPrecincts()) {
                temp.append(ps.getId()).append(":").append(c.getColor()).append(",");
            }
        }
        client.sendEvent("updateColor", temp.toString());
    }

    private void disconnectNeighborEdge(ClusterEdge desireClusterEdge, Cluster currentCluster, Cluster neighborCluster) {
        neighborCluster.removeEdge(desireClusterEdge);
        currentCluster.removeEdge(desireClusterEdge);
        currentCluster.removeDuplicateEdge(neighborCluster);//remove c4
    }

    private void combine(Cluster currentCluster, Cluster neighborCluster) {
        for (ClusterEdge edge : currentCluster.getAllEdges()) { //add edges(c5) from neighborCluster to currentCluster
            edge.changeNeighbor(edge.getNeighborCluster(currentCluster), neighborCluster);
            neighborCluster.addEdge(edge);
        }
        neighborCluster.combineCluster(currentCluster);//combine demo data
        for (ClusterEdge edge : neighborCluster.getAllEdges()) {//re-compute currentCluster edges join
            edge.computeJoin(currentState.getComunityOfinterest(), currentState.getConfiguration().getMajorMinorWeight());
        }
    }

    private void sendMove(Move move) {
        client.sendEvent("updateColor", move.getPrecinct().getId() + ":" + move.getTo().getCluster().getColor() + ",");
    }

    private void annealing() {
        Move move;
//        System.out.println("-12-3-1-23-1-23-1-2");
        int counter=0;
        int tempc=0;
        ArrayList<Move> moves=new ArrayList<>();
        while ((move = makeMove()) != null&&counter<600) {
            sendMove(move);

            System.out.println(counter++);
//            if(counter<20){
//                moves.add(move);
//                counter++;
//            }
//            else {
//                for(Move i:moves)
//                    sendMove(i);
//                System.out.println("send"+tempc);
//                tempc++;
//                moves=new ArrayList<>();
//                counter=0;
//            }
        }
//        for(Move i:moves)
//            sendMove(i);
//        moves=new ArrayList<>();
//        counter=0;
    }

    private Move makeMove() {
        District smallestDistrict = getSmallestDistrict(currentState.getDistricts());
        int equalPopulation = currentState.getPopulation() / currentState.getConfiguration().getTargetDistrictNumber();
        if (smallestDistrict.getPopulation() < equalPopulation) {
            Move bestMove;
            for (Precinct precinct : smallestDistrict.getBorderPrecincts()) {
                bestMove = getMove(smallestDistrict, precinct);
//                System.out.println("Bestmove:"+bestMove);
                if (bestMove != null) {
//                    if(bestMove.getPrecinct().getShape().getGeometryType().equals("MultiPolygon")){
//                        Set<PrecinctEdge> neighborEdge = bestMove.getPrecinct().getPrecinctEdges();
//                        ArrayList<Precinct> neighbors = new ArrayList<>();
//                        for(PrecinctEdge e : neighborEdge){
//                            Precinct nb = e.getNeighbor(bestMove.getPrecinct());
//                            if(bestMove.getPrecinct().getShape().contains(nb.getShape())){
//                                neighbors.add(nb);
//                            }
//                        }
//                        for(int i=0;i<neighbors.size();i++){
////                            District from = currentState.getDistricts().get(neighbors.get(i).getParentCluster());
//                            Move move = new Move(bestMove.getTo(),bestMove.getFrom(),neighbors.get(i));
//                            move.execute();
//                        }
//
//
//
//                    }
                    bestMove.execute();
                    return bestMove;
                }
            }
            return makeMove_secondary();
        }
        return null;
    }

    private Move getMove(District current, Precinct precinct) {
        Move bestMove = null;
        double bestImprovement = 0;
        for (Precinct otherDistrictPrecinct : precinct.getOtherDistrctPreicincts()) {
            double districtMajorMinorValue = current.getMajorMinor(currentState.getComunityOfinterest());
            double totalMajorMinorValue = precinct.getMajorMinor(currentState.getComunityOfinterest()) + districtMajorMinorValue;
            if (totalMajorMinorValue > currentState.getConfiguration().getMinMajorMinorPercent()
                    && totalMajorMinorValue < currentState.getConfiguration().getMaxMajorMinorPercent()) {
                District neighborDistrict = currentState.getFromDistrict(otherDistrictPrecinct);
                Move move = new Move(current, neighborDistrict, otherDistrictPrecinct);
                double improvement = testMove(move);
                System.out.println("improvement:"+improvement);
                if (improvement > bestImprovement) {
                    bestMove = move;
                    bestImprovement = improvement;
                }
            }
        }
        return bestMove;
    }

    private double testMove(Move move) {
        if (!isContiguity(move)) {
            return 0;
        }
        double initial_score = move.getTo().getCurrentScore() + move.getFrom().getCurrentScore();
        move.execute();
        double to_score = rateDistrict(move.getTo());
        double from_score = rateDistrict(move.getFrom());
        double final_score = to_score + from_score;
        double improvement = final_score - initial_score;
        move.undo();
        return improvement <= 0 ? 0 : improvement;
    }

    private Move makeMove_secondary() {
        List<District> districts = getSortedDistricts();
        districts.remove(0);//remove last round smallest district
        while (districts.size() > 0) {
            District startDistrict = districts.get(0);
            for (Precinct precinct : startDistrict.getBorderPrecincts()) {
                Move m = getMove(startDistrict, precinct);//......
                if (m != null) {
                    m.execute();
                    return m;
                }
            }
            districts.remove(0);
        }
        return null;
    }

    private District getSmallestDistrict(Map<String, District> districts) {
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

    private List<District> getSortedDistricts() {
        ArrayList<District> list = new ArrayList<>(currentState.getDistricts().values());
        Collections.sort(list);
        return list;
    }

    public void setWeights(HashMap<Measure, Double> w) {
        weights = w;
        currentScores = new HashMap<>();
        for (District d : currentState.getDistricts().values()) {
            currentScores.put(d, rateDistrict(d));
        }
    }

    private double rateDistrict(District d) {
        double score = 0;
        for (Measure m : weights.keySet()) {
            if (weights.get(m) != 0) {
                try {
                    Method rate = this.getClass().getDeclaredMethod(measures.get(m), District.class);
                    double rating = ((Double) rate.invoke(this, d));
                    rating = 1 - Math.pow((1 - rating), 2);
                    score += weights.get(m) * rating;
                } catch (Exception e) {
                    System.out.println(m.name() + " - " + e.getClass().getCanonicalName());
                    System.out.println(e.getMessage());
                    return -1;
                }
            }
        }
        return score;
    }

    public double calculateObjectiveFunction() {
        double score = 0;
        for (District d : currentState.getDistricts().values()) {
            score += currentScores.get(d);
        }
        return score;
    }

    public double ratePopequality(District d) {
        int idealPopulation = currentState.getPopulation() / currentState.getDistricts().size();
        int truePopulation = d.getPopulation();
        if (idealPopulation >= truePopulation) {
            return ((double) truePopulation) / idealPopulation;
        }
        return ((double) idealPopulation / truePopulation);
    }

    public double ratePartisanFairness(District d) {
        int totalVote = 0;
        int totalGOPvote = 0;
        int totalDistricts = 0;
        int totalGOPDistricts = 0;
        for (District sd : currentState.getDistricts().values()) {
            totalVote += sd.getGOPVote();
            totalVote += sd.getDEMVote();
            totalGOPvote += sd.getGOPVote();
            totalDistricts += 1;
            if (sd.getGOPVote() > sd.getDEMVote()) {
                totalGOPDistricts += 1;
            }
        }
        int idealDistrictChange = ((int) Math.round(totalDistricts * ((1.0 * totalGOPvote) / totalVote))) - totalGOPDistricts;
        if (idealDistrictChange == 0) {
            return 1.0;
        }
        int gv = d.getGOPVote();
        int dv = d.getDEMVote();
        int tv = gv + dv;
        int margin = gv - dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_v;
        if (idealDistrictChange * margin > 0) {
            inefficient_v = win_v - loss_v;
        } else {
            inefficient_v = loss_v;
        }
        return 1.0 - ((inefficient_v * 1.0) / tv);
    }

    private double rateCompactness(District d) {
        double allPrecincts = d.getCluster().getPrecincts().size();
        double borderPrecincts = d.getBorderPrecincts().size();
        return borderPrecincts / (allPrecincts - borderPrecincts);
    }

    private double calPolsbyPopperCompactness(District d){
        return (4*Math.PI*d.getShape().getArea())/ Math.pow(d.getShape().getLength(),2);
        //4pi*area of geometry / parameter^2
    }

    private double calSchwartzbergCompactness(District d){
        double radius = Math.sqrt(d.getShape().getArea()/Math.PI);
        double perimeter = 2 * Math.PI * radius;
        return 1/(d.getShape().getLength()/perimeter);
        //r = sqrt(A/PI)
        //C = 2*PI*r
        //1/(P/C)
    }

    private boolean isContiguity(Move move){
//        move.execute();
        Geometry fromDistrict = move.getFrom().getShape();
        // Geometry toDistrict = move.getTo().getShape();
        Geometry precinctShape = move.getPrecinct().getShape();
        if(precinctShape.getGeometryType().equals("MultiPolygon")){
            return false;
        }

        Geometry symDifference = fromDistrict.symDifference(precinctShape);
        // Geometry symDifferenceTo = toDistrict.symDifference(precinctShape);
        return !(symDifference.getGeometryType().equals("MultiPolygon"));
//        System.out.println("Contigunity "+move.getFrom().getDistrictID()+", "+symDifference.getGeometryType());
//        if(fromDistrict.getGeometryType().equals("MultiPolygon")){
//            move.undo();
//            return false;
//        }
//        move.undo();
//        return true;
    }

    public double rateStatewideEfficiencyGap(District d) {
        int iv_g = 0;
        int iv_d = 0;
        int tv = 0;
        for (District sd : currentState.getDistricts().values()) {
            int gv = sd.getGOPVote();
            int dv = sd.getDEMVote();
            if (gv > dv) {
                iv_d += dv;
                iv_g += gv - dv;
            } else if (dv > gv) {
                iv_g += gv;
                iv_d += dv - gv;
            }
            tv += gv;
            tv += dv;
        }
        return 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
    }

    public double rateEfficiencyGap(District d) {
        int gv = d.getGOPVote();
        int dv = d.getDEMVote();
        int tv = gv + dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_v = Math.abs(loss_v - (win_v - loss_v));
        return 1.0 - ((inefficient_v * 1.0) / tv);
    }

    public double rateCompetitiveness(District d) {
        return rateCompactness(d);
    }

    private String randomColor() {
        Random random = new Random();
        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(0xffffff + 1);
        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt);
        while (coloring.contains(colorCode)) {
            nextInt = random.nextInt(0xffffff + 1);
            // format it as hexadecimal string (with hashtag and leading zeros)
            colorCode = String.format("#%06x", nextInt);
        }
        coloring.add(colorCode);
        return colorCode;
    }

    public void run() {
        sendMessage("Algorithm Start...");
        init();
        graphPartition();
        currentState.initDistrict();
        annealing();
        updateDistrictBoundary();
        sendMessage("Algorithm finished!");
    }

    private void updateDistrictBoundary() {
        msg.append("'\n'");
        int districtNum = 1;
        for (District district : currentState.getDistricts().values()) {
            msg.append("No.").append(districtNum).append(": ").append(district.getdistrictID()).append(" : precinct size ").append(district.getCluster().getPrecincts().size()).append(", population ").append(district.getCluster().getDemographic().getPopulation()).append("'\n'");
            districtNum++;
        }
        StringBuilder districtColor = new StringBuilder();
        sendMessage("Assigning Colors...");
        StringBuilder districtJson = new StringBuilder();
        districtJson.append("{\"type\":\"FeatureCollection\", \"features\": [");
        for (Cluster c : currentState.getClusters().values()) {
            districtJson.append(c.toGeoJsonFormat()).append("},\n");
            for (Precinct ps : c.getPrecincts()) {
                districtColor.append(ps.getId()).append(":").append(c.getColor()).append(",");
            }
            client.sendEvent("updateColor", districtColor.toString());
            districtColor = new StringBuilder();
        }
        districtJson.deleteCharAt(districtJson.length() - 2).append("]}");
        sendDistrictBoundary(districtJson.toString());
        sendMessage(msg.toString());
    }

    public void batchRun() {
        for (Map.Entry<Integer, State> stateEntry : states.entrySet()) {
            currentState = stateEntry.getValue();
            run();
            sendMessage("batch run: " + stateEntry.getKey() + " finished!");
        }
        sendMessage("all batch run finished!");
    }

    private void sendMessage(String msg) {
        client.sendEvent("message", msg);
    }

    private void sendDistrictBoundary(String msg) {
        client.sendEvent("updateDistrictBoundary", msg);
    }
}
