package com.systouch.domain;

import com.systouch.dto.LoginDTORequest;
import com.systouch.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User {

    //Define o tipo ID como UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "userId")
    private UUID userId;

    // O nome de usuário será unico e exclusivo na tabela
    @Column(unique = true, name = "userName")
    private String userName;

    @Column(name = "userPassword")
    private String password;

    // "ManyToMany" anotação para dizer que o parametro "roles" terá
    // mais de um relacionamento, que está entre a coluna de "user_id" em "tb_users"
    // e também "role_id" em "tb_roles", onde a atribuição definida como "CascadeType.ALL
    // e FetchType.EAGER" diz que tudo e qualquer modificação que seja feita nessa clase de "User"
    // também refletirá de automático na tabela criada "tb_users_roles"
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(UserDTO dto){
        this.userName = dto.username();
        this.password = dto.password();
        this.roles = dto.roles();
    }

    public boolean isLoginCorrect(LoginDTORequest request, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(request.password(), this.password);

    }
}
