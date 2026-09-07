package com.ticketSystem.application.usecase.auth.login.exception;

import java.util.Set;

import com.ticketSystem.shared.exception.core.AppException;
import com.ticketSystem.shared.exception.core.ExceptionDefinition;

public final class InvalidCredentials extends AppException
{
    public InvalidCredentials()
    {
        super(new ExceptionDefinition(
            "Invalid credentials.",
            "The provided email or password is incorrect.",
            Set.of("The user may not exist.", "The password may be incorrect."),
            Set.of("Verify the email and password.", "Ensure the user exists in the system.")
        ));
    }
}
