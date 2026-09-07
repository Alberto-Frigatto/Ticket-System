package com.ticketSystem.domain.user;

import java.util.Objects;

import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.auth.UserCredentialsBuilder;

public final class User
{
    private final String id;
    private final String name;
    private final String email;
    private final Role role;
    private final Department department;

    protected User(
        String id,
        String name,
        String email,
        Role role,
        Department department
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.role = Objects.requireNonNull(role);
        this.department = Objects.requireNonNull(department);
    }

    public UserCredentials defineCredentials(String hashedPassword)
    {
        return UserCredentialsBuilder.of()
            .userId(this.getId())
            .hashedPassword(hashedPassword)
            .build();
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public Role getRole()
    {
        return role;
    }

    public Department getDepartment()
    {
        return department;
    }
}
