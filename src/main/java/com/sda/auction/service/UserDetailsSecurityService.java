package com.sda.auction.service;

import com.sda.auction.model.User;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsSecurityService implements UserDetailsService {

    // == fields ==
    private final UserRepository userRepository;

    // == constructor ==
    @Autowired
    public UserDetailsSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // == methods ==
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);

        if (!byEmail.isPresent()) { // if not present throw exception
            throw new UsernameNotFoundException(email);
        }

        User user = byEmail.get();
        Set<GrantedAuthority> roles = new HashSet<>(); // created a Set with GrantedAuthority
        roles.add(new SimpleGrantedAuthority(user.getRole().getName())); // we populated the Set with the roles name;

        // returns User from springframework.userdetails
        return new org.springframework.security.core.userdetails.
                User(user.getEmail(), user.getPassword(), roles);
    }

}
