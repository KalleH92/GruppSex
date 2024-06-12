package org.example.gruppsex.repository;

import jakarta.annotation.PostConstruct;
import org.example.gruppsex.model.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {

        MyUser ADMIN = new MyUser();
        ADMIN.setUsername("ad");
        ADMIN.setPassword("pass");
        ADMIN.setFirstName("Per");
        ADMIN.setLastName("Persson");
        ADMIN.setAge("45");
        ADMIN.setRole("ADMIN");

        userRepository.save(ADMIN);

    }

}
