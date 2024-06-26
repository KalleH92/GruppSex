package org.example.gruppsex.service;

/**
 * MaskUtils är en hjälparklass som tillhandahåller verktygsmetoder för att maskera känsliga data.
 *
 * Metoder:
 * - maskEmail: Maskerar en e-postadress genom att ersätta alla tecken i användarnamnet utom de första och sista med asterisker.
 *   - @param email: E-postadressen som ska maskeras.
 *   - @return: Den maskerade e-postadressen om den är tillräckligt lång för att maskeras korrekt; annars returneras den oförändrad.
 */

public class MaskUtils {
    public static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            // If the email is too short to mask properly
            return email;
        }

        String domain = email.substring(atIndex);
        String username = email.substring(0, atIndex);

        if (username.length() <= 2) {
            // If the username is too short to mask properly
            return email;
        }

        StringBuilder maskedUsername = new StringBuilder();
        maskedUsername.append(username.charAt(0)); // Keep the first character
        for (int i = 1; i < username.length() - 1; i++) {
            maskedUsername.append('*'); // Mask all characters except the first and last
        }
        maskedUsername.append(username.charAt(username.length() - 1)); // Keep the last character

        return maskedUsername.toString() + domain;
    }
}
