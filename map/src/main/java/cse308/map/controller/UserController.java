package cse308.map.controller;

import cse308.map.model.User;
import cse308.map.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/homepage")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signin")//check the user exist in the database or not
    public ResponseEntity<?> signIn(@Valid @RequestBody User user) {
        Boolean status = userService.isValidUser(user);//check if the user exist
        if (status) {
            Optional<User> opt = userService.findById(user.getEmail());
            System.out.println("sign in");
            return new ResponseEntity<User>(opt.get(), HttpStatus.OK);
        } else {
            System.out.println("user does not exist.");
            return new ResponseEntity("No such user ", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(value = "/signup")//save the state to the database
    public ResponseEntity<?> signUp(@Valid @RequestBody User user) {
        if(userService.findById(user.getEmail()).isPresent()){
            System.out.println("user already exist");
            return new ResponseEntity("user already exist ", HttpStatus.CONFLICT);//409
        }else {
            User newUser = userService.registerUser(user);//save the model into database
            System.out.println("register completed. ");
            return new ResponseEntity<User>(newUser, HttpStatus.OK);
        }
    }
}
