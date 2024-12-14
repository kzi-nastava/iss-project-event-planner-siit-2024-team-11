package org.example.eventy.users.services;

import org.example.eventy.users.models.Role;
import org.example.eventy.users.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService  {
    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        Role auth = this.roleRepository.getReferenceById(id);
        return auth;
    }

    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
