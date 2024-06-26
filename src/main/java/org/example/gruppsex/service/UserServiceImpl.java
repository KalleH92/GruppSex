
package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UpdateUserDTO;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * UserServiceImpl-klassen implementerar UserService-gränssnittet och tillhandahåller affärslogik för användarhantering.
 * Den ansvarar för att logga in, registrera, hämta, uppdatera och ta bort användare.
 *
 * Viktiga komponenter i denna klass:
 * - @Service: Markerar denna klass som en Spring-tjänstkomponent.
 * - userRepository: En instans av UserRepository som används för att utföra databasoperationer.
 * - passwordEncoder: En instans av PasswordEncoder som används för att kryptera lösenord.
 */

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
    public MyUser registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        try {
            MyUser user = new MyUser();
            user.setUsername(HtmlUtils.htmlEscape(userDTO.getUsername()));
            user.setPassword(passwordEncoder.encode(HtmlUtils.htmlEscape(userDTO.getPassword())));
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setRole("USER");
            System.out.println("user registered");
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Användarnamet upptaget");
        }

    }

    @Override
    public List<MyUser> getAllUsers() {
        System.out.println("got All users");
        return userRepository.findAll();
    }

    @Override
    public Optional<MyUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<MyUser> getUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return user;
        } else {
            throw new UsernameNotFoundException("Användare hittades inte: " + username);
        }

        // return userRepository.findByUsername(username);
    }

    @Override
    public Optional<MyUser> updateUser(Long id, UpdateUserDTO userDTO) {
        Optional<MyUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            MyUser user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            //user.setFirstName(userDTO.getFirstName());
            //user.setLastName(userDTO.getLastName());
            //user.setAge(userDTO.getAge());
            //user.setRole(userDTO.getRole());
            System.out.println("password updated");
            return Optional.of(userRepository.save(user));
        }
        throw new UsernameNotFoundException("Användare  hittades inte här heller: " + id);
        //return Optional.empty();
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