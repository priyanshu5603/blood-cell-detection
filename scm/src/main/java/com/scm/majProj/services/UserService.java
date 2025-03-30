package com.scm.majProj.services;

import java.util.List;

import com.scm.majProj.entities.User;

public interface UserService {
User saveUser(User user);
User getUserById(String id);
User getUserByEmail(String email);
User getUserByPhoneNumber(String phoneNumber);
User updateUser(User user);
void deleteUser(String id);
User verifyEmail(String email);
User verifyPhoneNumber(String phoneNumber);
List<User> getAllUsers();
}
