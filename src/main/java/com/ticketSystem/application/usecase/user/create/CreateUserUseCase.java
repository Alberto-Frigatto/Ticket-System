package com.ticketSystem.application.usecase.user.create;

import com.ticketSystem.application.port.data.user.UserCredentialsRepository;
import com.ticketSystem.application.port.data.user.UserRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.port.security.PasswordManager;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.application.usecase.user.create.exception.EmailAlreadyExists;
import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

public class CreateUserUseCase implements UseCase<CreateUserInput, User>
{
    private final IdGenerator idGenerator;
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordManager passwordManager;

    public CreateUserUseCase(
        IdGenerator idGenerator,
        UserRepository userRepository,
        UserCredentialsRepository userCredentialsRepository,
        PasswordManager passwordManager
    ) {
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordManager = passwordManager;
    }

    @Override
    public User execute(CreateUserInput input)
    {
        this.ensureEmailIsAvailable(input.email());

        User newUser =  UserBuilder.of()
            .id(idGenerator.generateId())
            .name(input.name())
            .email(input.email())
            .role(input.role())
            .department(input.department())
            .build();

        UserCredentials newUserCredentials = newUser.defineCredentials(
            passwordManager.hash(input.password())
        );

        this.userRepository.save(newUser);
        this.userCredentialsRepository.save(newUserCredentials);

        return newUser;
    }

    private void ensureEmailIsAvailable(String email)
    {
        if (this.userRepository.existsByEmail(email))
            throw new EmailAlreadyExists(email);
    }
}
