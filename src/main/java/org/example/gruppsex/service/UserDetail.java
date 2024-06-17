package org.example.gruppsex.service;

import org.example.gruppsex.model.MyUser;
import org.example.gruppsex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetail implements UserDetailsService {

    //@Autowired
    private final UserRepository userRepository;

    public UserDetail (UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    //private Set<GrantedAuthority> set = new HashSet<>();

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        MyUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
//
//
//        if (user.isEmpty()) {
//            new UsernameNotFoundException("User not exists with username: " + username);
//        }
//
//        GrantedAuthority authorities = new SimpleGrantedAuthority(user.get().getRole().toString());
//
//        //set.add(authorities);
//
//        return User.builder().username(user.get().getUsername())
//                .roles(user.get().getRole())
//                .password(user.get().getPassword())
//                .build();
//
//        //return new org.springframework.security.core.userdetails.User(username, user.get().getPassword(), set);
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("UserDetail: loadUserByUsername: (user attempted logging in)" + username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
