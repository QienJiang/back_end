package cse308.map.model;

public class Configuration {
    private int targetDistricteNumber;
    private int numOfRun;

    public Configuration(int desireNum) {
        this.targetDistricteNumber = desireNum;
    }

    public Configuration(int desireNum, int numOfRun) {
        this.targetDistricteNumber = desireNum;
        this.numOfRun=numOfRun;
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
}
