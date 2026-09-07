package com.ticketSystem.shared.exception;

import java.util.Set;

import com.ticketSystem.shared.exception.core.AppException;
import com.ticketSystem.shared.exception.core.ExceptionDefinition;

public final class ResourceNotFound extends AppException
{
    public ResourceNotFound(String resourceType, String resourceId)
    {
        super(new ExceptionDefinition(
            "Resource not found.",
            String.format("The resource '%s' with ID '%s' was not found.", resourceType, resourceId),
            Set.of("The resource may not exist.", "The resource ID may be incorrect."),
            Set.of("Verify the resource name and ID.", "Ensure the resource exists in the system.")
        ));
    }
}
