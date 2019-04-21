package cse308.map.model;


import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "State")
public class State {

    @Id
    private String id;
    private String name;
    private int population;
    private String rvote;
    private String dvote;

    public State(){

    }
    public State(Integer id, String name) {
        this.id = id.toString();
        this.name = name;
    }

    public String getRvote() {
        return rvote;
    }

    public void setRvote(String rvote) {

        this.rvote = rvote;
    }

    public String getDvote() {

        return dvote;
    }

    public void setDvote(String dvote) {

        this.dvote = dvote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}