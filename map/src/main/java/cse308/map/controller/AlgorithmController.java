package cse308.map.controller;

public class AlgorithmController {


    @GetMapping("/{phaseone}")//select the state with the specify id from the database
    public ResponseEntity<State> getStateInfo(@PathVariable("phaseone") Enum algorithm){
        Optional<State> opt = stateRepository.findById(id);
        if(!opt.isPresent()){
            return new ResponseEntity("No such element ",HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity<State>(opt.get(), HttpStatus.OK);
    }



}
