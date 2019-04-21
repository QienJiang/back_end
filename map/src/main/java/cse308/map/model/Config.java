package cse308.map.model;

public class Config {
    private int desireNum;
    private String stateId;

    public Config(int desireNum, String stateId) {
        this.desireNum = desireNum;
        this.stateId = stateId;
    }

    public int getDesireNum() {
        return desireNum;
    }

    public void setDesireNum(int desireNum) {
        this.desireNum = desireNum;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
