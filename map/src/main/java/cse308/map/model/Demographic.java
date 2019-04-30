package cse308.map.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Demographic {
    private int population;
    @Column(name = "democraticvote")
    private int democraticVote;
    @Column(name = "republicanvote")
    private int republicanVote;
    @Column(name = "nativeamerican")
    private int nativeAmerican;
    @Column(name = "africanamerican")
    private int africanAmerican;
    private int asian;

    public Demographic(){

    }

    public void combineDemo(Demographic demo) {
        population += demo.getPopulation();
        democraticVote += demo.getDemocraticVote();
        republicanVote += demo.getRepublicanVote();
        nativeAmerican += demo.getNativeAmerican();
        africanAmerican += demo.getAfricanAmerican();
        asian += demo.getAsian();
    }

    public void removeDemo(Demographic demo){
        population -= demo.getPopulation();
        democraticVote -= demo.getDemocraticVote();
        republicanVote -= demo.getRepublicanVote();
        nativeAmerican -= demo.getNativeAmerican();
        africanAmerican -= demo.getAfricanAmerican();
        asian -= demo.getAsian();
    }

    public int getMajorMinorPop(MajorMinor majorMinor){
        int[] majorMinorPops=new int[5];
        majorMinorPops[MajorMinor.NATIVEAMERICAN.ordinal()]=nativeAmerican;
        majorMinorPops[MajorMinor.AFRICANAMERICAN.ordinal()]=africanAmerican;
        majorMinorPops[MajorMinor.ASIAN.ordinal()]=asian;
        return majorMinorPops[majorMinor.ordinal()];
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getDemocraticVote() {
        return democraticVote;
    }

    public void setDemocraticVote(int democraticVote) {
        this.democraticVote = democraticVote;
    }

    public int getRepublicanVote() {
        return republicanVote;
    }

    public void setRepublicanVote(int republicanVote) {
        this.republicanVote = republicanVote;
    }

    public int getNativeAmerican() {
        return nativeAmerican;
    }

    public void setNativeAmerican(int nativeAmerican) {
        this.nativeAmerican = nativeAmerican;
    }

    public int getAfricanAmerican() {
        return africanAmerican;
    }

    public void setAfricanAmerican(int africanAmerican) {
        this.africanAmerican = africanAmerican;
    }

    public int getAsian() {
        return asian;
    }

    public void setAsian(int asian) {
        this.asian = asian;
    }
}
