package com.scm.majProj.services.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.majProj.entities.User;
import com.scm.majProj.helpers.ResourceNotFoundException;
import com.scm.majProj.repositories.UserRepo;
import com.scm.majProj.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
       }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));
     }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with email: " + email));
     }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserByPhoneNumber'");
    }

    @Override
    public User updateUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));
        userRepo.delete(user2); }

    @Override
    public User verifyEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyEmail'");
    }

    @Override
    public User verifyPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyPhoneNumber'");
    }

    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }

}
