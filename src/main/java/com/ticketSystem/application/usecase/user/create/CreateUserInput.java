package com.ticketSystem.application.usecase.user.create;

import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;

public record CreateUserInput(
    String name,
    String email,
    String password,
    Role role,
    Department department
) {

}
