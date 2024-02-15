package com.example.bookstoredemo.models;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    public final String firstName;
    public final String lastName;
    public final String email;
    public final String password;
}
