
package org.example.gruppsex.controller;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<MyUser>> getAllUsers() {
        List<MyUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<MyUser> getUserById(@PathVariable Long id) {
//        Optional<MyUser> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @PostMapping
    public ResponseEntity<MyUser> createUser(@RequestBody UserDTO userDTO) {
        MyUser user = userService.registerUser(userDTO);
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MyUser> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<MyUser> user = userService.updateUser(id, userDTO);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MyUser> loginUser(@RequestBody UserDTO userDTO) {
        MyUser user = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }
}