package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UpdateUserDTO;
import org.example.gruppsex.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    MyUser loginUser(String username, String rawPassword);
    MyUser registerUser(UserDTO userDTO);
    List<MyUser> getAllUsers();
    Optional<MyUser> getUserById(Long id);
    Optional<MyUser> updateUser(Long id, UpdateUserDTO userDTO);
    Optional<MyUser> getUserByUsername (String username);
    boolean deleteUser(Long id) throws UsernameNotFoundException;
}