package com.sda.auction.service;

import com.sda.repository.RoleRepository;
import com.sda.repository.UserRepository;
import com.sda.dto.UserDto;
import com.sda.mapper.UserMapper;
import com.sda.model.Role;
import com.sda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserMapper userMapper;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public void register(UserDto userDto) {
        User user = userMapper.map(userDto);
        assignRolesTo(user, userDto);
        encodePasswordFor(user);
        userRepository.save(user);
    }

    private void encodePasswordFor(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    private void assignRolesTo(User user, UserDto userDto) {
        Optional<Role> optionalRole;

        if (userDto.getIsAdmin()) {
            optionalRole = roleRepository.findByName("ROLE_ADMIN");
        } else {
            optionalRole = roleRepository.findByName("ROLE_PARTICIPANT");
        }

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            user.setRole(role);
        }
    }
}