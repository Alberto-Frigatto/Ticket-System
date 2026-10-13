package com.ticketSystem.application.usecase.auth;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.ticketSystem.application.port.data.user.UserCredentialsRepository;
import com.ticketSystem.application.port.data.user.UserRepository;
import com.ticketSystem.application.port.security.PasswordManager;
import com.ticketSystem.application.usecase.auth.login.LoginInput;
import com.ticketSystem.application.usecase.auth.login.LoginUseCase;
import com.ticketSystem.application.usecase.auth.login.exception.InvalidCredentials;
import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;
import com.ticketSystem.domain.user.UserBuilder;

public class LoginUseCaseTest
{
    @Mock UserRepository userRepository;
    @Mock UserCredentialsRepository userCredentialsRepository;
    @Mock PasswordManager passwordManager;

    LoginUseCase loginUseCase;

    @BeforeEach void setup()
    {
        MockitoAnnotations.openMocks(this);

        loginUseCase = new LoginUseCase(
            userRepository,
            userCredentialsRepository,
            passwordManager
        );
    }

    User buildValidUser()
    {
        return UserBuilder.of()
            .id("123")
            .name("John Doe")
            .email("john.doe@example.com")
            .role(Role.REQUESTER)
            .department(Department.IT)
            .build();
    }

    UserCredentials buildValidCredentials()
    {
        return buildValidUser().defineCredentials("hashed-password");
    }

    LoginInput buildValidInput()
    {
        return new LoginInput(
            "john.doe@example.com",
            "password"
        );
    }

    @Test
    void should_Login_When_ValidCredentialsAreProvided()
    {
        LoginInput input = buildValidInput();

        User user = buildValidUser();

        UserCredentials credentials = buildValidCredentials();

        when(userRepository.findByEmail(input.email()))
            .thenReturn(Optional.of(user));

        when(userCredentialsRepository.findByUserId(user.getId()))
            .thenReturn(Optional.of(credentials));

        when(passwordManager.verify(
            input.password(),
            credentials.getHashedPassword()
        )).thenReturn(true);

        User result = loginUseCase.execute(input);

        assertThat(result)
            .isNotNull()
            .isEqualTo(result);

        verify(userRepository).findByEmail(input.email());
        verify(userCredentialsRepository).findByUserId(user.getId());
        verify(passwordManager).verify(
            input.password(),
            credentials.getHashedPassword()
        );
    }

    @Test
    void should_Throw_When_UserDoesNotExist()
    {
        LoginInput input = buildValidInput();

        when(userRepository.findByEmail(input.email()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute(input))
            .isInstanceOf(InvalidCredentials.class);

        verify(userRepository).findByEmail(input.email());

        verify(userCredentialsRepository, never())
            .findByUserId(anyString());

        verify(passwordManager, never())
            .verify(anyString(), anyString());
    }

    @Test
    void should_Throw_When_UserCredentialsDoNotExist()
    {
        LoginInput input = buildValidInput();

        User user = buildValidUser();

        when(userRepository.findByEmail(input.email()))
            .thenReturn(Optional.of(user));

        when(userCredentialsRepository.findByUserId(user.getId()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute(input))
            .isInstanceOf(InvalidCredentials.class);

        verify(userRepository).findByEmail(input.email());
        verify(userCredentialsRepository).findByUserId(user.getId());
        verify(passwordManager, never())
            .verify(anyString(), anyString());
    }

    @Test
    void should_Throw_When_PasswordIsInvalid()
    {
        LoginInput input = buildValidInput();

        User user = buildValidUser();

        UserCredentials credentials = buildValidCredentials();

        when(userRepository.findByEmail(input.email()))
            .thenReturn(Optional.of(user));

        when(userCredentialsRepository.findByUserId(user.getId()))
            .thenReturn(Optional.of(credentials));

        when(passwordManager.verify(
            input.password(),
            credentials.getHashedPassword()
        )).thenReturn(false);

        assertThatThrownBy(() -> loginUseCase.execute(input))
            .isInstanceOf(InvalidCredentials.class);

        verify(userRepository).findByEmail(input.email());
        verify(userCredentialsRepository).findByUserId(user.getId());
        verify(passwordManager).verify(
            input.password(),
            credentials.getHashedPassword()
        );
    }
}
