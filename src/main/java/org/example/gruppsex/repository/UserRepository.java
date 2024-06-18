
package org.example.gruppsex.repository;

import org.example.gruppsex.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository är ett gränssnitt för datalageroperationer relaterade till MyUser-entiteten.
 * Det utökar JpaRepository som tillhandahåller grundläggande CRUD-operationer och ytterligare anpassade frågometoder.
 * *
 * Viktiga komponenter i denna klass:
 * - @Repository: Markerar detta gränssnitt som en Spring-repository-komponent.
 * - JpaRepository<MyUser, Long>: Utökar JpaRepository för att tillhandahålla CRUD-operationer för MyUser-entiteten med Long som ID-typ.
 *
 * Metoder:
 * - findByUsername: Hittar en användare baserat på användarnamnet.
 *   - @param username: Användarnamnet för att söka efter användaren.
 *   - @return: En Optional som innehåller användaren om den hittas, annars tom.
 */

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);
}