package com.ticketSystem.domain.user;

public final class UserBuilder
{
    private String id;
    private String name;
    private String email;
    private Role role;
    private Department department;

    private UserBuilder() {}

    public static UserBuilder of()
    {
        return new UserBuilder();
    }

    public UserBuilder id(String id)
    {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public UserBuilder email(String email)
    {
        this.email = email;
        return this;
    }

    public UserBuilder role(Role role)
    {
        this.role = role;
        return this;
    }

    public UserBuilder department(Department department)
    {
        this.department = department;
        return this;
    }

    public User build()
    {
        return new User(
            id,
            name,
            email,
            role,
            department
        );
    }
}
