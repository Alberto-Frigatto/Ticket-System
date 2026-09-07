package com.ticketSystem.shared.exception.core;

import java.util.Objects;
import java.util.Set;

public record ExceptionDefinition(
    String title,
    String detail,
    Set<String> causes,
    Set<String> solutions
) {
    public ExceptionDefinition
    {
        title = Objects.requireNonNull(title);
        detail = Objects.requireNonNull(detail);
        causes = Set.copyOf(Objects.requireNonNull(causes));
        solutions = Set.copyOf(Objects.requireNonNull(solutions));
    }
}
