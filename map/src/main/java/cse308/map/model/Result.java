package cse308.map.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Entity
public class Result extends ObjectMapper {
    @Id
    @GeneratedValue
    private Long id;
    private String email;


    @Lob
    private String stateJSON;

    @JsonIgnore
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customerAttributes;
    @Transient
    private ObjectMapper objectMapper = new ObjectMapper();


    public void serializeCustomerAttributes() throws JsonProcessingException {
        this.stateJSON = objectMapper.writeValueAsString(customerAttributes);
    }
    public void deserializeCustomerAttributes() throws IOException {
        this.customerAttributes = objectMapper.readValue(stateJSON, HashMap.class);
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


    public Map<String, Object> getCustomerAttributes() {
        return customerAttributes;
    }

    public void setCustomerAttributes(Map<String, Object> customerAttributes) {
        this.customerAttributes = customerAttributes;
    }
}
