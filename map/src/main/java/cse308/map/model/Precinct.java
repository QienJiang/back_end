package cse308.map.model;

import javax.persistence.Id;

public class Precinct {
    @Id
    private String id;

    public String getId() {
        return id;
    }
}
