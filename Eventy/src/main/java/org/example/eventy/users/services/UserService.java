package org.example.eventy.users.services;

import org.example.eventy.users.dtos.UserDTO;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User get(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean suspendUser(String userEmail, int daysDuration) {
        UserDTO user = getUserByEmail(userEmail);
        user.setName("SUSPENDED!");

        return true;
    }

    public UserDTO getUserByEmail(String userEmail) {
        UserDTO user = new UserDTO();
        user.setEmail(userEmail);

        return user;
    }

}
