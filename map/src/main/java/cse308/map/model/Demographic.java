package cse308.map.model;

public class Demographic {
    private Party politicalParty;
    private int population;
    private MajorMinor majorMinor;
    private int rvote;
    private int dvote;
    private int AFRICAN_AMERICAN;
    private int ASIAN_PACIFIC;
    private int HISPANIC;
    private int LATINO;
    private int NATIVAAMERICAN;

    public int getNATIVAAMERICAN() {
        return NATIVAAMERICAN;
    }

    public void setNATIVAAMERICAN(double NATIVAAMERICAN) {
        this.NATIVAAMERICAN = (int)NATIVAAMERICAN;
    }
    public void combinDemo(Demographic demo){
        population+= demo.getPopulation();
        rvote += demo.getRvote();
        dvote += demo.getDvote();
        AFRICAN_AMERICAN += demo.getAFRICAN_AMERICAN();
        ASIAN_PACIFIC += demo.getASIAN_PACIFIC();
        HISPANIC += demo.getHISPANIC();
        NATIVAAMERICAN += demo.NATIVAAMERICAN;

    }

    public Party getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(Party politicalParty) {
        this.politicalParty = politicalParty;
    }

    public MajorMinor getMajorMinor() {
        return majorMinor;
    }

    public void setMajorMinor(MajorMinor majorMinor) {
        this.majorMinor = majorMinor;
    }

    public int getRvote() {
        return rvote;
    }

    public void setRvote(int rvote) {
        this.rvote = rvote;
    }

    public int getDvote() {
        return dvote;
    }

    public void setDvote(int dvote) {
        this.dvote = dvote;
    }

    public int getAFRICAN_AMERICAN() {
        return AFRICAN_AMERICAN;
    }

    public void setAFRICAN_AMERICAN(int AFRICAN_AMERICAN) {
        this.AFRICAN_AMERICAN = AFRICAN_AMERICAN;
    }

    public int getASIAN_PACIFIC() {
        return ASIAN_PACIFIC;
    }

    public void setASIAN_PACIFIC(int ASIAN_PACIFIC) {
        this.ASIAN_PACIFIC = ASIAN_PACIFIC;
    }

    public int getHISPANIC() {
        return HISPANIC;
    }

    public void setHISPANIC(int HISPANIC) {
        this.HISPANIC = HISPANIC;
    }

    public int getLATINO() {
        return LATINO;
    }

    public void setLATINO(int LATINO) {
        this.LATINO = LATINO;
    }

    public Demographic(){
    }


    public int vote(Party pp) {
        if(pp == Party.DEMOCRATIC)
            return dvote;
        else if(pp == Party.REPUBLICAN)
            return dvote;
        else
            return 0;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
