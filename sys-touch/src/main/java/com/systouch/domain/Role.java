package com.systouch.domain;

import com.systouch.dto.RoleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name")
    private String name;

    // Criado um ENUM para conter as escolhas para cada Perfil de Usuário
    public enum Values {
        ADMIN(1L),
        BASIC(2L);

        // Id usado para receber a
        // escolha pra cada usuario escolhido
        long roleId;

        // Construtor para receber
        // o ID do tipo do usuário
        Values(long roleId){
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Role(RoleDTO dto){
        this.name = dto.name();
        this.Role.Values = dto.values();
    }*/


}
