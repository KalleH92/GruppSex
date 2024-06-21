package org.example.gruppsex.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UpdateUserDTO-klassen används för att överföra data vid uppdatering av en användares information.
 * Den innehåller valideringsregler för att säkerställa att data som tas emot är korrekt formaterad.
 *
 * Attribut:
 * - username: Användarens e-postadress som måste vara giltig och inte tom.
 * - password: Användarens lösenord som måste vara mellan 4 och 8 tecken långt.
 *
 * Valideringsregler:
 * - @NotBlank: Säkerställer att användarnamnet inte är tomt.
 * - @Email: Säkerställer att användarnamnet är en giltig e-postadress.
 * - @Size: Begränsar lösenordets längd till mellan 4 och 8 tecken.

 */

public class UpdateUserDTO {

    @NotBlank(message = "")
    @Email(message = "ange giltig email")
    private String username;

    @Size(min = 4, max = 8)
    private String password;

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
}
