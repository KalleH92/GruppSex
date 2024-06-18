package org.example.gruppsex.web;


import jakarta.validation.Valid;
import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.model.UpdateUserDTO;
import org.example.gruppsex.model.UserDTO;
import org.example.gruppsex.repository.UserRepository;
import org.example.gruppsex.service.MaskUtils;
import org.example.gruppsex.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ThymeleafController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ThymeleafController.class);


    public ThymeleafController(UserRepository userRepository, PasswordEncoder encoder, UserService userService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registreringsSida (Model model) {
        logger.debug("Registrerings-sida.");

        model.addAttribute("user", new UserDTO());

        return "registrera";
    }

    @PostMapping("/register")
    public String submitForm (@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        logger.debug("Registrerad användares information.");

        if (bindingResult.hasErrors()) {
            logger.debug("Fel information angiven baserad på DTOobjektets krav.");
            return "registrera";
        } else {
            logger.debug("Korrekt information baserad på DTOobjektets krav och lagt till användare " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(userDTO.getUsername())) + ".");

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
            //return "registrera";
            return "regsuc";

        }
    }

    @GetMapping("/list")
    public String userList (Model model) {
        logger.debug("Registrerade användare.");

        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/list/{id}")
    public String singleUser (@PathVariable Long id, Model model) {
        logger.debug("Information om användare med id: " + id + ".");

        boolean user = userService.getUserById(id).isPresent();

        if (user != false) {
            logger.debug("Användare med id " + id + " kunde hittas.");

            MyUser user1 = userService.getUserById(id).get();

            model.addAttribute("user", user1);

            return "user";

        } else {
            logger.warn("Användare med id " + id + " kunde ej hittas.");

            model.addAttribute("id", id);
            return "userNotFound";
        }

    }

    @PostMapping("/list/{id}")
    public String updateUser ( @PathVariable Long id, @Valid @ModelAttribute("user") /*UserDTO*/UpdateUserDTO user, BindingResult result, Model model, MyUser myUser) {
        logger.debug("Uppdatering av användares lösenord.");

        if (result.hasErrors()) {
            logger.debug("Lösenord ej uppdaterat då det ej uppfyller kraven.");
            return "user";
        } else {
            logger.debug("Lösenordet kan uppdateras.");

//        user.setAge(user.getAge());
//        user.setRole(user.getRole());
//        user.setUsername(user.getUsername());
//        user.setFirstName(user.getFirstName());
//        user.setLastName(user.getLastName());

        //userRepository.save(user);

        try {
            userService.updateUser(id, user);
        } catch (UsernameNotFoundException e) {
            logger.warn("Användaren " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user.getUsername())) + " hittades inte vid uppdatering.");
            logger.error("UserNameNotFoundException: ", e);
            //implement logger.warn
            //stack trace the exception e
            System.out.println("username not found when updating user");
        }

        //user.setId(myUser.getId());

        model.addAttribute("user", user);

        return "updateSuccess";
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteUser (@PathVariable("id") Long id, Model model) {

        boolean user = userRepository.findById(id).isPresent();/*orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));*/


        if (user == true) {

            MyUser user2 = userRepository.findById(id).get();

            userRepository.delete(user2);

            model.addAttribute("user", user2);

            return "userDeleted";

        } else {

            model.addAttribute("id", id);
            return "userNotFound";
        }
    }

    @GetMapping("/login")
    public String login () {
        logger.debug("Användarnamn och lösenord ska anges.");

        return "login";
    }

    @GetMapping("/"/*"/loginsuccess"*/)
    public String loggedInSuccessfully(Model model) {
        logger.debug("Inloggning har försökts.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            logger.debug("Principal är en User.");

            User user = (User) principal;
            MyUser user1 = userService.getUserByUsername(HtmlUtils.htmlEscape(user.getUsername())).orElse(null);

            if (user1 != null) {
                logger.debug("Inloggning för användare " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user1.getUsername())) + "lyckats.");
                model.addAttribute("user", user1);
                return "loginSuccess";
            }
        } else if (principal instanceof String) {
            logger.debug("Principal är en String.");
            String username = (String) principal;
            MyUser user1 = userService.getUserByUsername(HtmlUtils.htmlEscape(username)).orElse(null);

            if (user1 != null) {
                logger.debug("Inloggning för användare " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user1.getUsername())) + "lyckats.");
                model.addAttribute("user", user1);
                return "loginSuccess";
            }
        }
        logger.warn("Username och/eller Password finns ej.");
        return "login";
    }

    @GetMapping("/error")
    public String showError () {
        logger.debug("Error-sida.");
        return "error";
    }

    @GetMapping("/deleteuser")
    public String deleteUserForm (Model model) {
        logger.debug("Borttagning av användare sida.");

        model.addAttribute("user", new UserDTO());

        return "deleteuser";
    }

    @PostMapping("/deleteuser")
    public String deleteUser (@ModelAttribute("user") UserDTO user, Model model) {
        logger.debug("Logiken för att ta bort en användare.");


        try {

            MyUser user1 = userService.getUserByUsername(HtmlUtils.htmlEscape(user.getUsername())).get();

            //boolean user0 = userRepository.findByUsername(user.getUsername()).isPresent();

            //boolean user3 = userService.getUserByUsername(user.getUsername()).isPresent();

                if(user1.getRole() != "ADMIN") {
                    logger.debug("Användare har ej role ADMIN.");

                    System.out.println("user.getRole: " + user1.getRole());


                        userService.deleteUser(user1.getId());


                    //userRepository.delete(user1);

                    System.out.println("User deleted");
                    return "userDeleted";

                } else {

                    logger.warn("ADMIN kan ej bli borttagen.");
                    return "adminCantBeDeleted";
                }

            } catch (UsernameNotFoundException e) {
                logger.error("Ingen sån användare :(", e);
            }
                logger.warn("Användaren " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user.getUsername())) + " hittades inte vid borttagning.");
                model.addAttribute("id", HtmlUtils.htmlEscape(HtmlUtils.htmlEscape(user.getUsername())));
                return "userNotFound";
        //return "userNotFound";
    }

    @GetMapping("/updateuser")
    public String updatePasswordForm (Model model) {
        logger.debug("Uppdatering av användarens lösenord-sida.");

        model.addAttribute("user", new UpdateUserDTO());
        return "updateUser";
    }

    @PostMapping("/updateuser")
    public String updatePassword (@Valid @ModelAttribute("user") UpdateUserDTO user, BindingResult result, Model model, MyUser myUser) {
        logger.debug("Uppdatering av en användares lösenord har försökts.");

        if (result.hasErrors()) {
            logger.debug("Lösenordets validering misslyckades.");
            return "updateUser";
        }

        try {

            MyUser user1 = userService.getUserByUsername(HtmlUtils.htmlEscape(user.getUsername())).get();

            userService.updateUser(user1.getId(), user);

            model.addAttribute("user", user1);

            return "updateSuccess";

        } catch (UsernameNotFoundException e) {

            logger.debug("Användare har hittats.");
            logger.error("Ingen sån användare :(", e);
            logger.warn("Användare " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user.getUsername())) + "hittades inte vid uppdatering.");
            logger.error("UserNameNotFoundException: ", e);
            logger.error("Kollar vad e.getStackTrace är för något", e.getStackTrace());
            System.out.println("user with username not found");
        }

        logger.warn("Användare " + MaskUtils.maskEmail(HtmlUtils.htmlEscape(user.getUsername())) + "hittades inte vid uppdatering.");
        model.addAttribute("id", HtmlUtils.htmlEscape(user.getUsername()));
        return "userNotFound";

    }


//    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//
    @GetMapping("/logout")
    public String logout () {
        logger.debug("Utloggnings-sida");
        return "logout";
    }

    @PostMapping("/logout")
    public String performLogout () {
        logger.debug("Har loggats ut.");
        System.out.println("logged out");
        return "logoutSuccess";
    }

    @GetMapping("/logoutsuccess")
    public String getLogoutSuccess () {
        logger.debug("Meddelar lyckad utloggnings-sida.");
        return "logoutSuccess";
    }
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
