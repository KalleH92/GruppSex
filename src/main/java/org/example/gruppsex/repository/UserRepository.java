package org.example.gruppsex.repository;

import org.example.gruppsex.model.MyUser;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {
    @Query(value = "SELECT * FROM users WHERE username = :username AND password = :password", nativeQuery = true )
    MyUser loginUser(@Param("username") String username, @Param("password") String password);

    Optional<MyUser> findByUsername(String username);
}
