package cse308.map.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;


    @Lob
    private String stateJSON;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customerAttributes;


    public void serializeCustomerAttributes() throws JsonProcessingException {
        this.stateJSON = objectMapper.writeValueAsString(customerAttributes);
    }


    public String getStateJSON() {
        return stateJSON;
    }

    public void setStateJSON(String stateJSON) {
        this.stateJSON = stateJSON;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
