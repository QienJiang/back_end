package cse308.map.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "State")
public class State {

    @Id
    private Integer id;
    private String name;
    private String population;
    private String rvote;
    private String dvote;

    public State(int id, String name) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
}