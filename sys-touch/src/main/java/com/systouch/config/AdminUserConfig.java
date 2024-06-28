package com.systouch.config;

import com.systouch.domain.Role;
import com.systouch.domain.User;
import com.systouch.repository.RoleRepository;
import com.systouch.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {


    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Chamando metodo que pesquisa e retorna o tipo de usuário(ADMIN ou BASIC ou qualquer outro )
        var roleAdmin = this.roleRepository.findByName(Role.Values.ADMIN.name());

        // Faz uma pesquisa para saber se o usuario existe na base
        var userAdmin = this.userRepository.findByUserName("ADMIN");

        // Se o usuário existe na base:
        userAdmin.ifPresentOrElse(user -> {
            System.out.println("admin ja existe");
        },
                () -> {
                    var user = new User();
                    user.setUserName("admin");
                    user.setPassword(bCryptPasswordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    this.userRepository.save(user);
                });
    }
}
