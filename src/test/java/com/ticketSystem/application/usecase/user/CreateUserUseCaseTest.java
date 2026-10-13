package com.ticketSystem.application.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticketSystem.application.port.data.user.UserCredentialsRepository;
import com.ticketSystem.application.port.data.user.UserRepository;
import com.ticketSystem.application.port.id.IdGenerator;
import com.ticketSystem.application.port.security.PasswordManager;
import com.ticketSystem.application.usecase.user.create.CreateUserInput;
import com.ticketSystem.application.usecase.user.create.CreateUserUseCase;
import com.ticketSystem.application.usecase.user.create.exception.EmailAlreadyExists;
import com.ticketSystem.domain.auth.UserCredentials;
import com.ticketSystem.domain.user.Department;
import com.ticketSystem.domain.user.Role;
import com.ticketSystem.domain.user.User;

public class CreateUserUseCaseTest
{
    @Mock IdGenerator idGenerator;
    @Mock UserRepository userRepository;
    @Mock UserCredentialsRepository userCredentialsRepository;
    @Mock PasswordManager passwordManager;

    CreateUserUseCase useCase;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        useCase = new CreateUserUseCase(
            idGenerator,
            userRepository,
            userCredentialsRepository,
            passwordManager
        );
    }

    CreateUserInput buildValidInput()
    {
        return new CreateUserInput(
            "John Doe",
            "john.doe@example.com",
            "password",
            Role.REQUESTER,
            Department.IT
        );
    }

    @Test
    void should_CreateUser_When_ValidDataIsProvided()
    {
        CreateUserInput input = buildValidInput();

        when(userRepository.existsByEmail(input.email())).thenReturn(false);
        when(idGenerator.generateId()).thenReturn("123");
        when(passwordManager.hash(input.password())).thenReturn("hashed-password");

        User user = useCase.execute(input);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getRole()).isEqualTo(Role.REQUESTER);
        assertThat(user.getDepartment()).isEqualTo(Department.IT);

        verify(userRepository).save(user);
        verify(userCredentialsRepository).save(
            argThat(credentials ->
                credentials.getUserId().equals("123")
                    && credentials.getHashedPassword().equals("hashed-password")
            )
        );
        verify(passwordManager).hash(input.password());
    }

    @Test
    void should_Throw_When_EmailAlreadyExists()
    {
        CreateUserInput input = buildValidInput();

        when(userRepository.existsByEmail(input.email())).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(input))
            .isInstanceOf(EmailAlreadyExists.class);

        verify(userRepository, never()).save(any(User.class));
        verify(userCredentialsRepository, never()).save(any(UserCredentials.class));
        verify(idGenerator, never()).generateId();
        verify(passwordManager, never()).hash(anyString());
    }
}
