package com.ticketSystem.shared.exception.core;

import java.util.Map;
import java.util.Set;

public abstract class AppException extends RuntimeException
{
    private final String type;
    private final String title;
    private final String detail;
    private final Set<String> causes;
    private final Set<String> solutions;
    private Map<String, Object> extensions;

    public AppException(ExceptionDefinition definition)
    {
        super(definition.detail());

        this.type = this.toKebabCase(definition.title());
        this.title = definition.title();
        this.detail = definition.detail();
        this.causes = definition.causes();
        this.solutions = definition.solutions();
        this.extensions = Map.of();
    }

    public AppException(ExceptionDefinition definition, Map<String, Object> extensions)
    {
        super(definition.detail());

        this.type = this.toKebabCase(definition.title());
        this.title = definition.title();
        this.detail = definition.detail();
        this.causes = definition.causes();
        this.solutions = definition.solutions();
        this.extensions = Map.copyOf(extensions);
    }

    private String toKebabCase(String type)
    {
        return type
            .replaceAll("([a-z])([A-Z])", "$1-$2")
            .replaceAll("[\\s_]+", "-")
            .replaceAll("[^a-z-]", "")
            .toLowerCase();
    }

    public String getType()
    {
        return type;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDetail()
    {
        return detail;
    }

    public Set<String> getCauses()
    {
        return causes;
    }

    public Set<String> getSolutions()
    {
        return solutions;
    }

    public Map<String, Object> getExtensions()
    {
        return extensions;
    }
}
