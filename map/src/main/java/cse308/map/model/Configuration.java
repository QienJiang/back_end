package cse308.map.model;

import java.util.HashMap;

public class Configuration {
    private int targetDistrictNumber;
    private int numOfRun;
    private double majorMinor;
    private MajorMinor comunityOfinterest;
    private HashMap<Measure, Double> weights=new HashMap<>();

    public Configuration(){

    }
    public Configuration(int desireNum, int numOfRun, double majorminor) {
        this.targetDistrictNumber = desireNum;
        this.numOfRun = numOfRun;
        this.majorMinor = majorminor;
    }

    public Configuration(int desireNum, int numOfRun, double majorminor,String comunityOfinterest,
                         double POPULATION_EQUALITY,
                         double EFFICIENCY_GAP,
                         double COMPACTNESS,
                         double PARTISAN_FAIRNESS,
                         double COMPETITIVENESS) {
        this.targetDistrictNumber = desireNum;
        this.numOfRun = numOfRun;
        this.majorMinor = majorminor;
        this.comunityOfinterest=MajorMinor.valueOf(comunityOfinterest);
        weights.put(Measure.COMPACTNESS, COMPACTNESS);
        weights.put(Measure.POPULATION_EQUALITY, POPULATION_EQUALITY);
        weights.put(Measure.EFFICIENCY_GAP, EFFICIENCY_GAP);
        weights.put(Measure.PARTISAN_FAIRNESS, PARTISAN_FAIRNESS);
        weights.put(Measure.COMPETITIVENESS, COMPETITIVENESS);
    }

    public int getTargetDistrictNumber() {
        return targetDistrictNumber;
    }

    public void setTargetDistrictNumber(int desireNum) {
        this.targetDistrictNumber = desireNum;
    }

    public int getNumOfRun() {
        return numOfRun;
    }

    public void setNumOfRun(int numOfRun) {
        this.numOfRun = numOfRun;
    }

    public double getMajorMinorWeight() { return majorMinor; }

    public void setMajorminor(double majorminor) { this.majorMinor = majorminor; }

    public void setWeights(HashMap<Measure, Double> weights) {
        this.weights = weights;
    }

    public HashMap<Measure, Double> getWeights() {
        return weights;
    }

    public MajorMinor getComunityOfinterest() {
        return comunityOfinterest;
    }

    public void setComunityOfinterest(MajorMinor comunityOfinterest) {
        this.comunityOfinterest = comunityOfinterest;
    }
}
