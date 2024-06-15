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

@EnableWebSecurity
@Configuration
public class SecurityConfig {

//    @Autowired
//    private UserService userService;

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





//    @Bean
//    DaoAuthenticationProvider authenticationProvider () {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService( userService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }

//    @Bean
//    public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
//
//        return authConfig.getAuthenticationManager();
//
//    }

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
                        .permitAll());

        http.authenticationProvider(authenticationProvider()); // Seemingly optional

//        http.logout((logout) -> logout.logoutSuccessUrl("/logout")
//                .permitAll());
                //.httpBasic(httpSec -> httpSec.authenticationEntryPoint(unauthorizedEntryPoint)).userDetailsService(userDetail);

//                .formLogin(formLogin ->
//                        formLogin.loginPage("/login").defaultSuccessUrl("/list",true).failureUrl("/login?error=true")
//                                .permitAll());
                //.httpBasic(Customizer.withDefaults());

        //http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        //http.authenticationProvider(authenticationProvider());

        //http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetail); // Ensure userDetail is properly injected
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    @Bean
//    UserDetailsService userDetailsService () {
////        UserDetails user = User.builder()
////                .username("admin")
////                .password(passwordEncoder().encode("test"))
////                .roles("ADMIN")
////                .build();
////
////
////        return new InMemoryUserDetailsManager(user);
//        return userDetailsService();
//    }

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager () {
//
//        UserDetails admin = User.builder().username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin);
//    }

}
