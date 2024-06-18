package org.example.gruppsex.repository;

import jakarta.annotation.PostConstruct;
import org.example.gruppsex.model.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DatabaseInitializer-klassen initialiserar databasen med fördefinierade användare.
 * Denna klass körs vid applikationens start och skapar två användare: en administratör och en vanlig användare.
 *
 * Viktiga komponenter i denna klass:
 * - @Component: Markerar denna klass som en Spring-komponent, vilket gör att den kan upptäckas och hanteras av Spring-kontejnen.
 * - userRepository: En instans av UserRepository som används för att spara användare i databasen.
 * - encoder: En instans av PasswordEncoder som används för att kryptera lösenord.
 *
 * Metoder:
 * - init: Märkt med @PostConstruct, vilket innebär att den körs efter att beanen har konstruerats och alla beroenden har injicerats.
 *         Denna metod skapar och sparar två användare i databasen.
 */

@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {

        MyUser ADMIN = new MyUser();
        ADMIN.setUsername("admin@admin.com");
        ADMIN.setPassword(encoder.encode("pass"));
        ADMIN.setFirstName("Per");
        ADMIN.setLastName("Persson");
        ADMIN.setAge("45");
        ADMIN.setRole("ADMIN");


        MyUser user = new MyUser();
        user.setUsername("test@user.com");
        user.setPassword(encoder.encode("test"));
        user.setFirstName("Max");
        user.setLastName("Power");
        user.setAge("12");
        user.setRole("USER");

        userRepository.save(ADMIN);
        userRepository.save(user);

    }

}
