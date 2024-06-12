package org.example.gruppsex.web;


import jakarta.validation.Valid;
import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.example.gruppsex.service.Maskning;
import org.example.gruppsex.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

            user.setUsername(Maskning.maskEmail(userDTO.getEmail()));
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

    @PostMapping("/update/{id}")
    public String updateUser (@PathVariable Long id, @Valid MyUser user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "update_user";
        }

        userRepository.save(user);

        model.addAttribute("user", user);

        return "user";

    }

}
