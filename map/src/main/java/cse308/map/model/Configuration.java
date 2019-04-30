package cse308.map.model;

import java.util.HashMap;

public class Configuration {
    private int targetDistricteNumber;
    private int numOfRun;
    private double majorminor;
    private MajorMinor comunityOfinterest;
    private HashMap<Measure, Double> weights=new HashMap<>();


    public Configuration(int desireNum, int numOfRun, double majorminor) {
        this.targetDistricteNumber = desireNum;
        this.numOfRun = numOfRun;
        this.majorminor = majorminor;
    }
    public Configuration(int desireNum, int numOfRun, double majorminor,String comunityOfinterest,
                         double POPULATION_EQUALITY,
                         double EFFICIENCY_GAP,
                         double COMPACTNESS,
                         double PARTISAN_FAIRNESS,
                         double COMPETITIVENESS) {
        this.targetDistricteNumber = desireNum;
        this.numOfRun = numOfRun;
        this.majorminor = majorminor;
        this.comunityOfinterest=MajorMinor.valueOf(comunityOfinterest);
        weights.put(Measure.COMPACTNESS, COMPACTNESS);
        weights.put(Measure.POPULATION_EQUALITY, POPULATION_EQUALITY);
        weights.put(Measure.EFFICIENCY_GAP, EFFICIENCY_GAP);
        weights.put(Measure.PARTISAN_FAIRNESS, PARTISAN_FAIRNESS);
        weights.put(Measure.COMPETITIVENESS, COMPETITIVENESS);
    }


    public int getTargetDistricteNumber() {
        return targetDistricteNumber;
    }

    public void setTargetDistricteNumber(int desireNum) {
        this.targetDistricteNumber = desireNum;
    }

    public int getNumOfRun() {
        return numOfRun;
    }

    public void setNumOfRun(int numOfRun) {
        this.numOfRun = numOfRun;
    }

    public double getMajorMinorWeight() { return majorminor; }

    public void setMajorminor(double majorminor) { this.majorminor = majorminor; }

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
