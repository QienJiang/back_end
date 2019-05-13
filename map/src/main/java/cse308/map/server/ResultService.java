package cse308.map.server;

import cse308.map.model.Result;
import cse308.map.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.map.model.Result;
import java.io.Serializable;
import java.util.Optional;

@Service
public class ResultService{
    @Autowired
    private ResultRepository resultRepository;

    public Optional<Result> findById(long stateId){
        return  resultRepository.findById(stateId);
    }

    public void saveState(Result state){
        resultRepository.save(state);
    }
}
