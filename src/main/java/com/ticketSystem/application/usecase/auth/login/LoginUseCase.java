package com.ticketSystem.application.usecase.auth.login;

import com.ticketSystem.application.port.data.user.UserCredentialsRepository;
import com.ticketSystem.application.port.data.user.UserRepository;
import com.ticketSystem.application.port.security.PasswordManager;
import com.ticketSystem.application.usecase.auth.login.exception.InvalidCredentials;
import com.ticketSystem.application.usecase.core.UseCase;
import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.user.User;

public final class LoginUseCase implements UseCase<LoginInput, User>
{
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordManager passwordManager;

    public LoginUseCase(
        UserRepository userRepository,
        UserCredentialsRepository userCredentialsRepository,
        PasswordManager passwordManager
    ) {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordManager = passwordManager;
    }

    @Override
    public User execute(LoginInput input)
    {
        User user = this.searchUser(input.email());
        UserCredentials credentials = this.searchCredentials(user);

        this.ensurePasswordIsValid(credentials.getHashedPassword(), input.password());

        return user;
    }

    private User searchUser(String email)
    {
        return this.userRepository.findByEmail(email)
            .orElseThrow(InvalidCredentials::new);
    }

    private UserCredentials searchCredentials(User user)
    {
        return this.userCredentialsRepository.findByUserId(user.getId())
            .orElseThrow(InvalidCredentials::new);
    }

    private void ensurePasswordIsValid(String hashedPassword, String plainPassword)
    {
        if (!this.passwordManager.verify(plainPassword, hashedPassword))
            throw new InvalidCredentials();
    }
}
