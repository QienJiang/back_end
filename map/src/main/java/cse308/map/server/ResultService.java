package cse308.map.server;

import cse308.map.model.Result;
import cse308.map.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.map.model.Result;
import java.io.Serializable;

@Service
public class ResultService extends Result{
    @Autowired
    private ResultRepository resultRepository;

    public Iterable<Result> findById(String stateId){
        return  resultRepository.findAll();
    }

    public void saveState(Result state){
        resultRepository.save(state);
    }
}
