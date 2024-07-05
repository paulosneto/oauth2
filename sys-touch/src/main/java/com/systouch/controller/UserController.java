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

    // Transação que irá criar o usuário e armazenar no banco
    @Transactional
    @PostMapping
    public ResponseEntity<User> newUser(@RequestBody UserDTO dto){

        // Cria a regra para armazenar em usuários do tipo padão "BASIC"
        var basicRole = this.roleRepository.findByName(Role.Values.BASIC.name());

        // Pesquisa se o usuário logado existe no banco
        var userFromDB = this.userRepository.findByUserName(dto.username());
        User newUser;

        // Verificação para saber se existe cadastro para o usuario, se existir, retorna erro
        if(userFromDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            // Registra o usuario caso o mesmo não exista no banco.
             newUser = new User(dto);
                newUser.setUserName(dto.username());
                newUser.setPassword(bCryptPasswordEncoder.encode(dto.password()));
                newUser.setRoles(Set.of(basicRole));
            this.userRepository.save(newUser);
        }

        return ResponseEntity.ok().body(newUser);

    }


    // Lista somente se os usuarios forem ADMIN
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers(){
        var users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }



}
