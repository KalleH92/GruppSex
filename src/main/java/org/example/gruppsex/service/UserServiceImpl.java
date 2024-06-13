
package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public MyUser loginUser(String username, String rawPassword) {

        Optional<MyUser> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                System.out.println("user logged in");
                return user;
            }
        }
        return null;
    }

    @Override
    public MyUser registerUser(UserDTO userDTO) {
        MyUser user = new MyUser();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setRole(userDTO.getRole());
        System.out.println("user registered");
        return userRepository.save(user);
    }

    @Override
    public List<MyUser> getAllUsers() {
        System.out.println("got All users");
        return userRepository.findAll();
    }

    @Override
    public MyUser getUserById(Long id) {
        Optional<MyUser> user = userRepository.findById(id);
        System.out.println("user with id found: " + id);
        return user.orElseThrow(() ->new RuntimeException("User not found with id: " + id));
    }

    @Override
    public Optional<MyUser> updateUser(Long id, UserDTO userDTO) {
        Optional<MyUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            MyUser user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setRole(userDTO.getRole());
            System.out.println("password updated");
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            System.out.println("user with id deleted: " + id);
            return true;
        }
        return false;
    }

}