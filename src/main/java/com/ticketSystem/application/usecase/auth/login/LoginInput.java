package com.ticketSystem.application.usecase.auth.login;

public record LoginInput(
    String email,
    String password
) {

}
