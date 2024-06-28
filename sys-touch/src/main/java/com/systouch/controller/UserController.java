package com.systouch.controller;

import com.systouch.domain.Role;
import com.systouch.domain.User;
import com.systouch.dto.UserDTO;
import com.systouch.repository.RoleRepository;
import com.systouch.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@EnableMethodSecurity
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<User> newUser(@RequestBody UserDTO dto){

        var basicRole = this.roleRepository.findByName(Role.Values.BASIC.name());
        //String t = dto.roles().;
        //var basicRole = this.roleRepository.findByName(dto.roles().toString());

        var userFromDB = this.userRepository.findByUserName(dto.username());
        User newUser;

        if(userFromDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
             newUser = new User(dto);
                newUser.setUserName(dto.username());
                newUser.setPassword(bCryptPasswordEncoder.encode(dto.password()));
                newUser.setRoles(Set.of(basicRole));
            this.userRepository.save(newUser);
        }

        return ResponseEntity.ok().body(newUser);

    }


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers(){
        var users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }



}
