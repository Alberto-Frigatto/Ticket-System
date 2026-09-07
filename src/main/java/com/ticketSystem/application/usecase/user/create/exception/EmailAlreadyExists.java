package com.ticketSystem.application.usecase.user.create.exception;

import java.util.Set;

import com.ticketSystem.shared.exception.core.AppException;
import com.ticketSystem.shared.exception.core.ExceptionDefinition;

public final class EmailAlreadyExists extends AppException
{
    public EmailAlreadyExists(String email)
    {
        super(new ExceptionDefinition(
            "Email already exists.",
            String.format("The email %s is already associated with another user.", email),
            Set.of("The email may be already registered.", "The user may have forgotten their credentials."),
            Set.of("Use a different email address.", "Recover the account associated with this email.")
        ));
    }
}
