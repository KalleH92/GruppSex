package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * UserDetail-klassen implementerar UserDetailsService och används för att ladda användardetaljer vid inloggning.
 * Den hämtar användarinformation från databasen och omvandlar den till ett format som används av Spring Security.
 *
 * Viktiga komponenter i denna klass inkluderar:
 * - @Service: Markerar denna klass som en Spring-tjänstkomponent.
 * - userRepository: En instans av UserRepository som används för att hämta användare från databasen.
 *
 * Metoder:
 * - loadUserByUsername: Laddar användardetaljer baserat på användarnamnet.
 *   - @param username: Användarnamnet för att söka efter användaren.
 *   - @return: Ett UserDetails-objekt som innehåller användarens information.
 *   - @throws UsernameNotFoundException: Kastas om ingen användare med det angivna användarnamnet hittas.
 */

@Service
public class UserDetail implements UserDetailsService {

    //@Autowired
    private final UserRepository userRepository;

    public UserDetail (UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("UserDetail: loadUserByUsername: (user attempted logging in)" + username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
