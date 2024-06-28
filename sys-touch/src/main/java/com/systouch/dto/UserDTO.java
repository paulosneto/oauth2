package com.systouch.dto;

import com.systouch.domain.Role;

import java.util.Set;

public record UserDTO(String username, String password, Set<Role> roles) {

}
