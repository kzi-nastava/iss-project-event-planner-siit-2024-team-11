package org.example.eventy.users.services;

import org.example.eventy.users.dtos.UserDTO;
import org.example.eventy.users.dtos.UserType;
import org.example.eventy.users.models.Admin;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public User get(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        }
        catch (Exception e) {
            return null;
        }
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

    public UserType getUserType(User user) {
        if(user instanceof EventOrganizer) {
            return UserType.ORGANIZER;
        } else if(user instanceof SolutionProvider) {
            return UserType.PROVIDER;
        } else if(user instanceof Admin) {
            return UserType.ADMIN;
        }

        return UserType.AUTHENTICATED;
    }
}
