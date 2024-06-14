package org.example.gruppsex.web;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UpdateUserDTO;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.example.gruppsex.service.Maskning;
import org.example.gruppsex.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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

            //MyUser user = new MyUser();

            //user.setUsername(HtmlUtils.htmlEscape(userDTO.getUsername())); // added HTMLUtils
            //user.setPassword(HtmlUtils.htmlEscape(encoder.encode(userDTO.getPassword())));
            //user.setFirstName(userDTO.getFirstName());
            //user.setLastName(userDTO.getLastName());
            //user.setAge(userDTO.getAge());
            //user.setRole("USER");
            //userRepository.save(user);

            userService.registerUser(userDTO);

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

        boolean user = userService.getUserById(id).isPresent();

        if (user != false) {

            MyUser user1 = userService.getUserById(id).get();

            model.addAttribute("user", user1);

            return "user";

        } else {

            model.addAttribute("id", id);
            return "userNotFound";
        }

    }

    @PostMapping("/list/{id}")
    public String updateUser ( @PathVariable Long id, @Valid @ModelAttribute("user") /*UserDTO*/UpdateUserDTO user, BindingResult result, Model model, MyUser myUser) {

        if (result.hasErrors()) {
            return "user";
        } else {

//        user.setAge(user.getAge());
//        user.setRole(user.getRole());
//        user.setUsername(user.getUsername());
//        user.setFirstName(user.getFirstName());
//        user.setLastName(user.getLastName());

        //userRepository.save(user);

        try {
            userService.updateUser(id, user);
        } catch (UsernameNotFoundException e) {
            //implement logger.warn
            //stack trace the exception e
            System.out.println("username not found when updating user");
        }

        //user.setId(myUser.getId());

        model.addAttribute("user", user);

        return "updateSuccess";
        }

    }

//    @GetMapping("/delete/{id}")
//    public String deleteUser (@PathVariable("id") Long id, Model model) {
//
//        boolean user = userRepository.findById(id).isPresent();/*orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));*/
//
//
//        if (user == true) {
//
//            MyUser user2 = userRepository.findById(id).get();
//
//            userRepository.delete(user2);
//
//            model.addAttribute("user", user2);
//
//            return "userDeleted";
//
//        } else {
//
//            model.addAttribute("id", id);
//            return "userNotFound";
//        }
//    }

    @GetMapping("/login")
    public String login () {

        return "login";
    }

    @GetMapping("/loginsuccess")
    public String loggedInSuccessfully (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        //MyUser user2 = userRepository.findByUsername(user.getUsername()).get();
        MyUser user1 = userService.getUserByUsername(user.getUsername()).get();

        model.addAttribute("user", user1);

        return "loginSuccess";
    }

    @GetMapping("/error")
    public String showError () {
        return "error";
    }

    @GetMapping("/deleteuser")
    public String deleteUserForm (Model model) {

        model.addAttribute("user", new UserDTO());

        return "deleteuser";
    }

    @PostMapping("/deleteuser")
    public String deleteUser (@ModelAttribute("user") UserDTO user, Model model) {


        //boolean user0 = userRepository.findByUsername(user.getUsername()).isPresent();

        boolean user3 = userService.getUserByUsername(user.getUsername()).isPresent();

        if (user3 != false) {

            MyUser user1 = userService.getUserByUsername(HtmlUtils.htmlEscape(user.getUsername())).get();

            if(user1.getRole() != "ADMIN") {

                System.out.println("user.getRole: " + user1.getRole());

                try {
                    userService.deleteUser(user1.getId());
                } catch (UsernameNotFoundException e) {
                    // insert logger.warn
                    // stack trace the exception e
                    System.out.println("username not found");

                    model.addAttribute("id", user.getUsername());
                    return "userNotFound";
                }

                //userRepository.delete(user1);

                return "userDeleted";

            } else {

                return "adminCantBeDeleted";
            }

        } else {

            model.addAttribute("id", user.getUsername());
            return "userNotFound";
        }

    }

    @GetMapping("/updateuser")
    public String updatePasswordForm (Model model) {

        model.addAttribute("user", new UpdateUserDTO());
        return "updateUser";
    }

    @PostMapping("/updateuser")
    public String updatePassword (@Valid @ModelAttribute("user") UpdateUserDTO user, BindingResult result, Model model, MyUser myUser) {

        if (result.hasErrors()) {
            return "updateUser";
        }

        boolean userExists = userService.getUserByUsername(user.getUsername()).isPresent();

        if (userExists) {
            MyUser user1 = userService.getUserByUsername(user.getUsername()).get();

            try {
                userService.updateUser(user1.getId(), user);
            } catch (UsernameNotFoundException e) {
                //logger.warn
                //stack trace exception e
                System.out.println("user with username not found");
            }


            model.addAttribute("user", user1);

            return "updateSuccess";

        } else {

            model.addAttribute("id", user.getUsername());
            return "userNotFound";
        }

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
