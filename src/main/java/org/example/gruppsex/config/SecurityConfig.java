package org.example.gruppsex.config;


import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.service.UserDetail;
import org.example.gruppsex.service.UserService;
import org.example.gruppsex.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * SecurityConfig-klassen konfigurerar säkerhetsinställningar för applikationen.
 * Den använder Spring Security för att hantera autentisering och auktorisering.
 *
 * Viktiga komponenter i denna klass:
 * - @Configuration och @EnableWebSecurity: Markerar denna klass som en konfigurationsklass för säkerhet.
 * - UserDetail: Används för att hämta användardetaljer.
 *
 * Beans:
 * - webSecurityCustomizer: Ignorerar säkerhetsinställningar för vissa URL-mönster, såsom "/h2-console/**".
 * - passwordEncoder: Kodar lösenord med BCryptPasswordEncoder.
 * - securityFilterChain: Konfigurerar HTTP-säkerhet, inklusive vilka URL:er som ska tillåtas och vilka som kräver autentisering.
 * - authenticationProvider: Konfigurerar en DaoAuthenticationProvider med en UserDetailsService och PasswordEncoder.
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig {



    @Autowired
    private UserDetail userDetail;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", /*"/register",*/ "/update/**", /*"/list/**",*/ "/update/**"/*, "/delete/**"*/);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http/*csrf(csrf -> csrf.ignoringRequestMatchers("/register"))*/
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest.requestMatchers("/admin")
                                .hasRole("ADMIN")
                                .requestMatchers("/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/register").hasRole("ADMIN")
                                .requestMatchers("/list").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/list/**").hasRole("ADMIN")
                                .requestMatchers("/deleteuser").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated())
                .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/logoutsuccess").permitAll())
                //.httpBasic(Customizer.withDefaults());
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/"/*"/loginsuccess"*/)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .httpBasic(Customizer.withDefaults());

        http.authenticationProvider(authenticationProvider()); // Seemingly optional



        return http.build();

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetail); // Ensure userDetail is properly injected
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



}
