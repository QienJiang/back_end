package cse308.map.server;


import cse308.map.model.User;
import cse308.map.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveOrUpdateUser(User user){

        if(user.getPassword()==null||user.getEmail()==""){
            user.setPassword("TO_DO");
        }
        return userRepository.save(user);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(String email){
        return  userRepository.findById(email);
    }
    public void deleted(String email){
        userRepository.deleteById(email);
    }
}
