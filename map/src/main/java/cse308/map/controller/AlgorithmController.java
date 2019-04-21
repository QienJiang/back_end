package cse308.map.controller;

import com.corundumstudio.socketio.SocketIOClient;
import cse308.map.algorithm.Algorithm;
import cse308.map.model.AlgoType;
import cse308.map.model.Config;
import cse308.map.model.State;
import cse308.map.server.PrecinctService;
import cse308.map.server.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@CrossOrigin
public class AlgorithmController {

    @Autowired
    private StateService stateService;
    @Autowired
    private PrecinctService precinctService;

    @PostMapping(value = "/run")//select the state with the specify id from the database
    public ResponseEntity<?> runAlgorithm(@RequestBody Config stateConfig, SocketIOClient client){
        System.out.println("xxxxx algorithm");
        System.out.println("state id: "+stateConfig.getStateId());
        Iterable<State> opt = stateService.findById(stateConfig.getStateId());

        System.out.println();

        //hardcode
        stateConfig.setDesireNum(10);
        Algorithm algorithm = new Algorithm("pa",stateConfig.getDesireNum(),1,precinctService);
        algorithm.run(client);


        return new ResponseEntity<State>(opt.iterator().next(), HttpStatus.OK);
    }



}
