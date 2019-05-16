package cse308.map.controller;

import cse308.map.model.User;
import cse308.map.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/homepage")
@CrossOrigin
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/admin")//check the user exist in the database or not
    public ResponseEntity<?> findAllUsers() {
        Iterable<User> users = userService.findAll();
        List<User> userList  = new ArrayList<>();
        for(User user : users){
            userList.add(user);
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping(value = "/delete")//check the user exist in the database or not
    public ResponseEntity<?> deletedUsers(User user) {
        userService.isValidUser(user);
        userService.deleted(user.getEmail());
        return new ResponseEntity("finish deleted user", HttpStatus.OK);
    }

    @PostMapping(value = "/update")//check the user exist in the database or not
    public ResponseEntity<?> updatedUsers(User user) {
        userService.isValidUser(user);
        userService.deleted(user.getEmail());
        userService.registerUser(user);
        return new ResponseEntity("finish deleted user", HttpStatus.OK);
    }
}
