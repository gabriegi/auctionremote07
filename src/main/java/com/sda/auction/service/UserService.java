package com.sda.auction.service;

import com.sda.auction.dto.UserDto;
import com.sda.auction.mapper.UserMapper;
import com.sda.auction.model.Role;
import com.sda.auction.model.User;
import com.sda.auction.repository.RoleRepository;
import com.sda.auction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    // == fields ==
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // == constructor ==
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // == public methods ==
    public void register(UserDto userDto) {
        User user = userMapper.map(userDto);
        assignRolesTo(user, userDto); // method to assign roles
        encodePasswordFor(user); // method to encode pass
        userRepository.save(user);
    }

    // == private methods ==
    private void assignRolesTo(User user, UserDto userDto) {
        Optional<Role> optionalRole;
        if (userDto.getIsAdmin()) {
            optionalRole = roleRepository.findByName("ROLE_ADMIN");
        } else {
            optionalRole = roleRepository.findByName("ROLE_PARTICIPANT");
        }

        // if role is present in the Optional<Role> we get the role and assign it to the user
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            user.setRole(role);
        }
    }

    private void encodePasswordFor(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }
}
