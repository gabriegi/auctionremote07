package com.sda.service;

import com.sda.model.User;
import com.sda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsSecurityService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //implementarea noastra pe care io dam Springului (cand vrea sa faca logarea , o face pe baza datelor bagate de noi
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(email);
        }
        User user = optionalUser.get();
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPassword(), roles);
    }


}
