package com.ticketSystem.application.port.security;

public interface PasswordManager
{
    String hash(String password);
    boolean verify(String plainPassword, String hashedPassword);
}
