package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    MyUser loginUser(String username, String rawPassword);
    MyUser registerUser(UserDTO userDTO);
    List<MyUser> getAllUsers();
    MyUser getUserById(Long id);
    Optional<MyUser> updateUser(Long id, UserDTO userDTO);
    boolean deleteUser(Long id);
}