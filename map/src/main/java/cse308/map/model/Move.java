package cse308.map.model;

import com.vividsolutions.jts.geom.Geometry;

import java.util.Objects;

public class Move {

    private District to;
    private District from;
    private Precinct precinct;
    public Move(District to, District from, Precinct precinct){
        this.to = to;
        this.from = from;
        this.precinct = precinct;
    }





    public void execute(){
        from.removePrecinct(precinct);
        Geometry newDistrict = from.getCluster().getShape().symDifference(precinct.getShape());
        from.getCluster().setShape(newDistrict);
        to.addPrecinct(precinct);
        newDistrict = to.getCluster().getShape().union(precinct.getShape());
        from.getCluster().setShape(newDistrict);
    }

    public void undo(){
        to.removePrecinct(precinct);
        from.addPrecinct(precinct);
    }

    public District getTo(){
        return to;
    }
    public District getFrom(){
        return from;
    }
    public Precinct getPrecinct(){
        return precinct;
    }

//    public double rateDistrict(){}

}
