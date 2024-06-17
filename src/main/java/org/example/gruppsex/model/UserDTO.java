package org.example.gruppsex.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Value;

public class UserDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank (message = "Ange en epost adress")
    @Email(message = "Ange giltig epost adress")
    private String username;

    @NotBlank (message = "")
    @Size(min = 4, max = 8, message = "Lösenord måste vara mellan 4-8 bokstäver")
    private String password;

    @NotBlank (message = "Ange ett förnamn")
    private String firstName;

    @NotBlank (message = "Ange ett efternamn")
    private String lastName;

    @NotBlank (message = "Ange en ålder")
    @Min(value = 1, message = "Ange en giltig ålder")
    private String age;

    private String role;

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
