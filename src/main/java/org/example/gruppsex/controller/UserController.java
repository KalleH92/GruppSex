
package org.example.gruppsex.controller;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.service.UserAlreadyExistsException;
import org.example.gruppsex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController-klassen hanterar HTTP-förfrågningar relaterade till användarhantering.
 * Den tillhandahåller slutpunkter för att hämta alla användare, skapa en ny användare, ta bort en användare och logga in en användare.
 *
 * Viktiga komponenter i denna klass inkluderar:
 * - @Controller: Markerar denna klass som en Spring MVC-kontroller.
 * - @RequestMapping: Specificerar bas-URL för alla metoder i denna klass.
 * - userService: En instans av UserService för att hantera affärslogik relaterad till användare.
 *
 * Metoder:
 * - getAllUsers: Hämtar och returnerar en lista med alla användare.
 * - createUser: Skapar en ny användare baserat på den information som skickas i begäransens kropp.
 * - deleteUser: Tar bort en användare med angivet ID.
 * - loginUser: Hanterar inloggning för en användare baserat på användarnamn och lösenord.
 */

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<MyUser>> getAllUsers() {
        List<MyUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    @PostMapping
    public ResponseEntity<MyUser> createUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistsException {
        MyUser user = userService.registerUser(userDTO);
        return ResponseEntity.status(201).body(user);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<MyUser> loginUser(@RequestBody UserDTO userDTO) {
//        MyUser user = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
//        if (user != null) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//    }
}