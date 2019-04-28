package cse308.map.model;

public class Configuration {
    private int targetDistricteNumber;
    private int numOfRun;
    private double majorminor;

    public Configuration(int desireNum) {
        this.targetDistricteNumber = desireNum;
    }

    public Configuration(int desireNum, int numOfRun, double majorminor) {
        this.targetDistricteNumber = desireNum;
        this.numOfRun = numOfRun;
        this.majorminor = majorminor;
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

    public double getMajorminor() { return majorminor; }

    public void setMajorminor(double majorminor) { this.majorminor = majorminor; }
}
