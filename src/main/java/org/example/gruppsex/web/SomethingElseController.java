package org.example.gruppsex.web;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.example.gruppsex.service.Maskning;
import org.example.gruppsex.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SomethingElseController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;


    public SomethingElseController(UserRepository userRepository, PasswordEncoder encoder, UserService userService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registreringsSida (Model model) {

        model.addAttribute("user", new UserDTO());

        return "registrera";
    }

    @PostMapping("/register")
    public String submitForm (@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registrera";
        } else {

            MyUser user = new MyUser();

            user.setUsername(userDTO.getUsername());
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setRole("USER");
            userRepository.save(user);

            model.addAttribute("user", userDTO);
            //System.out.println();
            return "registrera";

        }
    }

    @GetMapping("/list")
    public String userList (Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/list/{id}")
    public String singleUser (@PathVariable Long id, Model model) {

        MyUser user = userService.getUserById(id);

        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/list/{id}")
    public String updateUser ( @PathVariable Long id, @Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model, MyUser myUser) {

        if (result.hasErrors()) {
            return "user";
        } else {

//        user.setAge(user.getAge());
//        user.setRole(user.getRole());
//        user.setUsername(user.getUsername());
//        user.setFirstName(user.getFirstName());
//        user.setLastName(user.getLastName());


        //userRepository.save(user);
        userService.updateUser(id, user);

        user.setId(myUser.getId());

        model.addAttribute("user", user);

        return "updateSuccess";
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteUser (@PathVariable("id") Long id, Model model) {

        MyUser user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

        if (user != null) {
            userRepository.delete(user);

            model.addAttribute("user", user);

            return "userDeleted";

        } else {

            return "userNotFound";
        }
    }

    @GetMapping("/login")
    public String login () {

        return "login";
    }

    @GetMapping("/loginsuccess")
    public String loggedInSuccessfully (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        MyUser user2 = userRepository.findByUsername(user.getUsername()).get();

        model.addAttribute("user", user2);

        return "loginSuccess";
    }

    @GetMapping("/error")
    public String showError () {
        return "error";
    }

//    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//
//    @GetMapping("/logout")
//    public String logout () {
//        return "logout";
//    }
//
//    @PostMapping("/logout")
//    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//        // .. perform logout
//        this.logoutHandler.logout(request,response,authentication);
//        return "redirect:/logout";
//    }
//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute("user") @RequestBody UserDTO userDTO) {
//        MyUser user = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
//        if (user != null) {
//            return "registrera";
//        } else {
//            return "list"; // Unauthorized
//        }
//    }

}
