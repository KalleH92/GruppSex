package org.example.gruppsex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//@Getter
//@Setter

/**
 * MyUser-klassen representerar en användare i systemet.
 * Denna klass är en entitet som mappas till tabellen "UserData" i databasen.
 * Den innehåller information om användaren såsom användarnamn, lösenord, förnamn, efternamn, ålder och roll.
 *
 * Viktiga komponenter i denna klass:
 * - @Entity: Markerar denna klass som en JPA-entitet.
 * - @Id och @GeneratedValue: Anger att 'id' är primärnyckeln och att dess värde genereras automatiskt.
 * - Serializable: Gör det möjligt att serialisera objekt av denna klass.
 */

@Entity(name="UserData")
public class MyUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String age;
    private String role;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
