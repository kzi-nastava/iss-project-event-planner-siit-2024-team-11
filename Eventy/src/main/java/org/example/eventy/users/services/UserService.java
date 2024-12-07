package org.example.eventy.users.services;

import org.example.eventy.users.dtos.ReportDTO;
import org.example.eventy.users.dtos.UserDTO;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    /*@Autowired
    private UserRepository userRepository;*/

    public EventOrganizer getEventOrganizer(Long id) {
        return new EventOrganizer();
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
