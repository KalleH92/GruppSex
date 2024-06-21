package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UpdateUserDTO;
import org.example.gruppsex.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * UserService är ett gränssnitt som definierar affärslogiken för användarhantering.
 * Den tillhandahåller metoder för att logga in, registrera, hämta, uppdatera och ta bort användare.
 *
 * Metoder:
 * - loginUser: Loggar in en användare baserat på användarnamn och okrypterat lösenord.
 * - registerUser: Registrerar en ny användare baserat på UserDTO.
 * - getAllUsers: Hämtar en lista över alla användare.
 * - getUserById: Hämtar en användare baserat på ID.
 * - updateUser: Uppdaterar en användare baserat på ID och UpdateUserDTO.
 * - getUserByUsername: Hämtar en användare baserat på användarnamn.
 * - deleteUser: Tar bort en användare baserat på ID.
 */

public interface UserService {
    MyUser loginUser(String username, String rawPassword);
    MyUser registerUser(UserDTO userDTO) throws UserAlreadyExistsException;
    List<MyUser> getAllUsers();
    Optional<MyUser> getUserById(Long id);
    Optional<MyUser> updateUser(Long id, UpdateUserDTO userDTO);
    Optional<MyUser> getUserByUsername (String username);
    boolean deleteUser(Long id) throws UsernameNotFoundException;
}