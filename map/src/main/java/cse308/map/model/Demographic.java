package cse308.map.model;

public class Demographic {
    private Party politicalParty;
    private int population;
    private MajorMinor majorMinor;
    private int[] mmPopulation;
    private int[] votePopulation;
//    private int rvote;
//    private int dvote;

    public Demographic(MajorMinor mm, int pop, int mmPop) {
        this.majorMinor = mm;
        this.population = pop;
        this.mmPopulation=new int[MajorMinor.values().length];
        this.mmPopulation[mm.ordinal()]=mmPop;
    }

    public int[] getAllmmPopulation(){
        return mmPopulation;
    }

    public int getMmPopulation(){
        return mmPopulation[majorMinor.ordinal()];
    }

    public int[] getAllVotePopulation() {
        return votePopulation;
    }

    public int getVotePopulation(){
        return votePopulation[politicalParty.ordinal()];
    }

    public void combinDemo(Demographic demo) {
        population += demo.getPopulation();
//        for(int v=0;v<demo.getAllVotePopulation().length;v++){
//            this.getAllVotePopulation()[v]=demo.getAllVotePopulation()[v];
//        }
        for(int i=0;i<demo.getAllmmPopulation().length;i++){
            this.mmPopulation[i]+=demo.getAllmmPopulation()[i];
        }
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



//    public int getAFRICAN_AMERICAN() {
//        return AFRICAN_AMERICAN;
//    }
//
//    public void setAFRICAN_AMERICAN(int AFRICAN_AMERICAN) {
//        this.AFRICAN_AMERICAN = AFRICAN_AMERICAN;
//    }
//
//    public int getASIAN_PACIFIC() {
//        return ASIAN_PACIFIC;
//    }
//
//    public void setASIAN_PACIFIC(int ASIAN_PACIFIC) {
//        this.ASIAN_PACIFIC = ASIAN_PACIFIC;
//    }
//
//    public int getHISPANIC() {
//        return HISPANIC;
//    }
//
//    public void setHISPANIC(int HISPANIC) {
//        this.HISPANIC = HISPANIC;
//    }
//
//    public int getLATINO() {
//        return LATINO;
//    }
//
//    public void setLATINO(int LATINO) {
//        this.LATINO = LATINO;
//    }

    public Demographic() {
    }



    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


    public void setMmPopulation(int[] mmPopulation) {
        this.mmPopulation = mmPopulation;
    }
}
