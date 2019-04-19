package cse308.map.model;

public class Demographic {
    private Party politicalParty;
    private int population;
    private MajorMinor mm;
    private int rvote;
    private int dvote;
    private int afpop;
    private int appop;
    private int hispop;
    private int lapop;

    public Demographic(){
    }

    public void setAfpop(int afpop) {
        this.afpop = afpop;
    }

    public int getAfpop() {
        return afpop;
    }

    public int getAppop() {
        return appop;
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
