package com.ticketSystem.application.usecase.core;

public interface UseCase<Input, Output>
{
    Output execute(Input input);
}
